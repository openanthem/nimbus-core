/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutor.Output;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandExecutorGateway {

	@Getter @Setter @RequiredArgsConstructor @ToString
	public static class MultiOutput {
		
		private final String inputCommandUri;
		
		private List<Output<?>> outputs;
		
		private ValidationResult validation;
		private ExecuteError error;
		
		@JsonIgnore
		private final CollectionsTemplate<List<Output<?>>, Output<?>> template = CollectionsTemplate.linked(this::getOutputs, this::setOutputs);
	}

	default MultiOutput execute(Command cmd, String payload) {
		return execute(new CommandMessage(cmd, payload));
	}
	
	public MultiOutput execute(CommandMessage cmdMsg);
	
}
