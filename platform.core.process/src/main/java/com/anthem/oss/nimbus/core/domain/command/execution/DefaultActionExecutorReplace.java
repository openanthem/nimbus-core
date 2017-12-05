/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultActionExecutorReplace extends AbstractCommandExecutor<Boolean> {
	
	public DefaultActionExecutorReplace(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		
		Object state = getConverter().convert(p.getConfig(), eCtx.getCommandMessage().getRawPayload());
		
		p.setState(state);
		
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
} 
