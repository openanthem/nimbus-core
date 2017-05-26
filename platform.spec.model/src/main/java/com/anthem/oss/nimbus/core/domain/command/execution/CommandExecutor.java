/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutor<R> {

	@Getter @RequiredArgsConstructor @ToString
	public static class ActionBehavior {
		
		private final Action action;
		private final Behavior behavior;
	}
	
	@Getter @ToString(callSuper=true)
	public static class Input extends ActionBehavior {
		
		private final ExecutionContext context;
		
		public Input(Action action, Behavior b, ExecutionContext context) {
			super(action, b);
			this.context = context;
		}
	}
	
	@Getter @Setter @ToString(callSuper=true)
	public static class Output<T> extends ActionBehavior {
		
		private T value;
		
		private ValidationResult validation;
		private ExecuteError error;
		
		public Output(Action action, Behavior b) {
			super(action, b);
		}
	}
	
	public Output<R> execute(Input input);
}
