/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface BPMGateway {

	public String startBusinessProcess(ExecutionContext eCtx,String processId);
	
	public Object continueBusinessProcessExecution(ExecutionContext eCtx, String processExecutionId);
}
