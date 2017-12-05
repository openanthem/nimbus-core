/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionBehaviorExecutorGetState extends AbstractCommandExecutor<Object> {


	public DefaultActionBehaviorExecutorGetState(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected Output<Object> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		Object entityState = p.getLeafState();
		
		return Output.instantiate(input, eCtx, entityState);
	}

}
