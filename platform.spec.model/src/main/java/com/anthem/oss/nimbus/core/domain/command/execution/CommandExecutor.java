/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutor<R> {
	
	public Output<R> execute(Input input);
}
