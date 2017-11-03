/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutorGateway {


	default MultiOutput execute(Command cmd, String payload) {
		return execute(new CommandMessage(cmd, payload));
	}
	
	MultiOutput execute(CommandMessage cmdMsg);
	
	List<MultiOutput> executeConfig(ExecutionContext eCtx, Param<?> cmdParam, List<Execution.Config> execConfigs);
	
}
