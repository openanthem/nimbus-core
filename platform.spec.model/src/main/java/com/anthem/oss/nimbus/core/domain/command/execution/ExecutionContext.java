/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor @ToString
public class ExecutionContext {

	private final CommandMessage commandMessage;
	
	@Getter(value=AccessLevel.PRIVATE)
	private final QuadModel<?, ?> quadModel;
	
	public ExecutionModel<?> getRootModel(){
		return quadModel.getRoot();
	}
}
