/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.execution;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@ToString
public class MultiExecuteOutput extends ExecuteOutput<Map<Integer, ExecuteOutput.BehaviorExecute<?>>> {

	private static final long serialVersionUID = 1L;

	
	public MultiExecuteOutput() {}
	
	public MultiExecuteOutput(ExecuteOutput.BehaviorExecute<?> output) {
		this.add(output);
	}
	
	
	/**
	 * 
	 * @param output
	 */
	public void add(ExecuteOutput.BehaviorExecute<?> output) {
		createOrGet().put(createOrGet().size(), output);
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<Integer, ExecuteOutput.BehaviorExecute<?>> createOrGet() {
		if(getResult() == null) {
			Map<Integer, ExecuteOutput.BehaviorExecute<?>> result = new HashMap<>();
			setResult(result);
		} 
		return getResult();	
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> T getSingleResult() {
		if(getResult() == null) return null;
		
		if(getResult().size() > 1) throw new IllegalStateException("Execute output contains more than one result elements: "+getResult());
		
		return (T) this.getResult().values().iterator().next().getResult();
	}
	
}
