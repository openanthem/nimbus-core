/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ProcessEngineContext implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private transient Object output;
	
	private transient Object input;

	private transient ExecutionContext executionContext;
	
	private transient Param<?> actionParam;
	
	public ProcessEngineContext(){}
	
	public ProcessEngineContext(ExecutionContext executionContext, Param<?> actionParam){
		this.executionContext = executionContext;
		this.actionParam = actionParam;
	}
	
	public boolean isOutputAnException() {
		return output != null && output instanceof Exception;
	}
}
