/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface BPMGateway {

	public ProcessResponse startBusinessProcess(ExecutionContext eCtx,String processId, Param<?> actionParameter);
	
	public Object continueBusinessProcessExecution(ExecutionContext eCtx, String processExecutionId, Param<?> actionParameter);
}
