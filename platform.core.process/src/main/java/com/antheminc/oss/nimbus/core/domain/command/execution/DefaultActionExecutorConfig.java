/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorConfig extends AbstractCommandExecutor<EntityConfig<?>> {
	
	public DefaultActionExecutorConfig(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	/**
	 * Returns {@linkplain ModelConfig} for domain root call, otherwise, would return {@linkplain ParamConfig} for nested domain command path
	 */
	@Override
	protected Output<EntityConfig<?>> executeInternal(Input input) {
		Param<?> p = findParamByCommandOrThrowEx(input.getContext());

		return Output.instantiate(input, input.getContext(), p.getConfig());
	}
} 