/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ExecutionContext {

	private final CommandMessage commandMessage;
	
	private QuadModel<?, ?> quadModel;
	
	public ExecutionContext(CommandMessage commandMessage) {
		this.commandMessage = commandMessage;
	}
		
	public ExecutionContext(CommandMessage commandMessage, QuadModel<?, ?> quadModel) {
		this(commandMessage);
		setQuadModel(quadModel);
	}
	
	public String getId() {
		return getCommandMessage().getCommand().getRootDomainUri();
	}
	
	public ExecutionModel<?> getRootModel(){
		return quadModel.getRoot();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other==null)
			return false;
		
		if(!(other instanceof ExecutionContext))
			return false;
		
		ExecutionContext otherCtx = (ExecutionContext)other;
		
		String thisDomainRootUri = getId();
		String otherDomainRootUri = otherCtx.getId();
		
		return thisDomainRootUri.equals(otherDomainRootUri);
	}
	
	@Override
	public String toString() {
		return getId();
	}
}
