/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.config.EntityConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultBehaviorExecutorConfig extends AbstractCommandExecutor<EntityConfig<?>> {
	
	public DefaultBehaviorExecutorConfig(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	/**
	 * Returns {@linkplain ModelConfig} for domain root call, otherwise, would return {@linkplain ParamConfig} for nested domain command path
	 */
	@Override
	protected Output<EntityConfig<?>> executeInternal(Input input) {
		
		EntityConfig<?> config = findConfigByCommand(input.getContext());
		
		return Output.instantiate(input, input.getContext(), config);
	}
} 