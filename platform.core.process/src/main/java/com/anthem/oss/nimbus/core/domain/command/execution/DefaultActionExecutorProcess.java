/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultActionExecutorProcess<T,R> extends AbstractFunctionCommandExecutor<T,R> {
	
	private BPMGateway bpmGateway;
	
	public DefaultActionExecutorProcess(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.bpmGateway = beanResolver.get(BPMGateway.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		R response = containsFunctionHandler(input) ? (R)executeFunctionHanlder(input, FunctionHandler.class) : continueBusinessProcessExceution(input.getContext());
		return Output.instantiate(input, input.getContext(), response);
	}
	
	@SuppressWarnings("unchecked")
	private R continueBusinessProcessExceution(ExecutionContext eCtx){
		QuadModel<?,?> quadModel = getQuadModel(eCtx);
		String processExecutionId = quadModel.getFlow().getProcessExecutionId();
		return (R)bpmGateway.continueBusinessProcessExecution(eCtx.getRootModel().getAssociatedParam(), processExecutionId);
	}

}
