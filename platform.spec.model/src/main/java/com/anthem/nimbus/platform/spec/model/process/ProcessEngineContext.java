/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
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
	
	private transient Object model;
	
	public ProcessEngineContext(){}
	
	public ProcessEngineContext(ExecutionContext executionContext, Param<?> actionParam){
		this.executionContext = executionContext;
		this.actionParam = actionParam;
		this.model = executionContext.getQuadModel().getView();
	}
	
	public boolean isOutputAnException() {
		return output != null && output instanceof Exception;
	}
	
	public Object getParamValue(String paramId){
		return executionContext.getQuadModel().getView().findParamByPath(paramId).getState();
	}
	
	public Object getParamValueFromParam(Param<?> param, String paramId){
		return param.findParamByPath(paramId).getState();
	}
}