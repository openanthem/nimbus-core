/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.bpm.BPMGateway;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.antheminc.oss.nimbus.core.domain.command.execution.fn.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class StatelessBPMFunctionHanlder<T,R> extends AbstractFunctionHandler<T, R> {
	
	private BPMGateway bpmGateway;
	
	public StatelessBPMFunctionHanlder(BeanResolverStrategy beanResolver) {
		this.bpmGateway = beanResolver.find(BPMGateway.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		String processId = executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_EXECUTE_PROCESS_ID.code);
		ProcessResponse response = bpmGateway.startStatlessBusinessProcess(executionContext.getQuadModel().getView().getAssociatedParam(), processId);
		return (R)response.getResponse();
	}
}
