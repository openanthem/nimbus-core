/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutorGateway {


	default MultiOutput execute(Command cmd, String payload) {
		return execute(new CommandMessage(cmd, payload));
	}
	
	public MultiOutput execute(CommandMessage cmdMsg);
	
}
