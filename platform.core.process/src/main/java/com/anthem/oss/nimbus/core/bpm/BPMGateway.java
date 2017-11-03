/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface BPMGateway {

	public ProcessResponse startBusinessProcess(Param<?> param,String processId);
	
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId);
}
