/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Optional;

import org.assertj.core.util.Arrays;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.InvalidOperationAttemptedException;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
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
public class ConfigConditionalStateChangeHandler extends AbstractConditionalStateEventHandler implements OnStateChangeHandler<ConfigConditional> {

	private CommandExecutorGateway commandGateway;
	
	public ConfigConditionalStateChangeHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
	}
	
	@Override
	public void handle(ConfigConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		boolean isTrue = evalWhen(event.getParam(), configuredAnnotation.when());
		
		if(!isTrue)
			return;
		
		Config[] configs = configuredAnnotation.config();
		Optional.ofNullable(configs).filter(c->!Arrays.isNullOrEmpty(c)).orElseThrow(()->new InvalidConfigException("No @Config found to execute conditionnaly on param: "+event.getParam()));
		
		throw new InvalidOperationAttemptedException("impl in progress...");
	}
}
