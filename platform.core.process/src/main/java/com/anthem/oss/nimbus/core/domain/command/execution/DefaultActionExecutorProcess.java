/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultActionExecutorProcess<T,R> extends AbstractFunctionCommandExecutor<T,R> {
	
	public DefaultActionExecutorProcess(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		R response = (R)executeFunctionHanlder(input, FunctionHandler.class);
		return Output.instantiate(input, input.getContext(), response);
	}

}
