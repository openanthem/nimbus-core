/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.command;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.dsl.Behavior;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class ExecuteOutput<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private T result;
	
	private ValidationResult validationResult;
	
	private ExecuteError executeException;
	
	
	public ExecuteOutput() { }
	
	public ExecuteOutput(T result) { 
		setResult(result);
	}
	
	
	
	@Getter @Setter @ToString(callSuper=true) @RequiredArgsConstructor
	public static class BehaviorExecute<T> extends ExecuteOutput<T> {
		
		private static final long serialVersionUID = 1L;
		
		final private Behavior b;
		
		
		public BehaviorExecute(Behavior b, T result) {
			this(b);
			setResult(result);
		}
	}
	
}
