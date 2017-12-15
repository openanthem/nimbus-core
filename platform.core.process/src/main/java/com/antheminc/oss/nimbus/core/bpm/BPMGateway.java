/**
 * 
 */
package com.antheminc.oss.nimbus.core.bpm;

import com.antheminc.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.entity.process.ProcessFlow;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface BPMGateway {

	public ProcessFlow startBusinessProcess(Param<?> param,String processId);
	
	public ProcessResponse startStatlessBusinessProcess(Param<?> param,String processId);
	
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId);
}