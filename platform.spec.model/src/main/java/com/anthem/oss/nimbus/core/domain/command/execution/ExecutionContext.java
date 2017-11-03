/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ExecutionContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private final CommandMessage commandMessage;
	
	private QuadModel<?, ?> quadModel;
	
	public ExecutionContext(Command command) {
		this(new CommandMessage(command, null));
	}
	
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
	
	public boolean equalsId(Command cmd) {
		return StringUtils.equals(getId(), cmd.getRootDomainUri());
	}
	
	public ExecutionModel<?> getRootModel(){
		return quadModel.getRoot();
	}
	
	public <P> Param<P> findParamByPath(String path) {
		return getRootModel().findParamByPath(path);
	}
	
	public <P> P findStateByPath(String path) {
		return getRootModel().findStateByPath(path);
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
