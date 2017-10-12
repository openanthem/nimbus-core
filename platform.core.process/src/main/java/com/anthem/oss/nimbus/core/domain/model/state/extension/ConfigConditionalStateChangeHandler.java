/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;


import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContextLoader;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.extension.ConfigConditional;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;

/**
 * @author Soham Chakravarti
 *
 */
@RefreshScope
public class ConfigConditionalStateChangeHandler extends AbstractConditionalStateEventHandler implements OnStateChangeHandler<ConfigConditional> {

	private CommandExecutorGateway commandGateway;
	
	private ExecutionContextLoader contextLoader;
	
	private boolean initialized;
	
	public ConfigConditionalStateChangeHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	public void init() {
		if(initialized)
			return;
		
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
		this.contextLoader = beanResolver.get(ExecutionContextLoader.class);
	}
	
	@Override
	public void handle(ConfigConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		init();
		
		boolean isTrue = evalWhen(event.getParam(), configuredAnnotation.when());
		
		if(!isTrue)
			return;
		
		Config[] configs = configuredAnnotation.config();
		Optional.ofNullable(configs).filter(ArrayUtils::isNotEmpty).orElseThrow(()->new InvalidConfigException("No @Config found to execute conditionnaly on param: "+event.getParam()));
		
		Command rootCmd = event.getParam().getRootExecution().getRootCommand();
		ExecutionContext eCtx = contextLoader.load(rootCmd);
		
		commandGateway.executeConfig(eCtx, event.getParam(), Arrays.asList(configs));
	}
}
