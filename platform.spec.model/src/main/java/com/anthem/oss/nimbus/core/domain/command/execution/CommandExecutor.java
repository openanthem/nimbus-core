/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutor<R> {
	
	public Output<R> execute(Input input);
}
