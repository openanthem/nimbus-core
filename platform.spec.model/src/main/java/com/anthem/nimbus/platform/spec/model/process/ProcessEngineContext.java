/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandElementLinked;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ProcessEngineContext implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private CommandMessage commandMessage;

	private transient Object output;
	
	private transient Object input;

	private transient QuadModel<?,?> quadModel;
	
	private transient Param<?> actionParam;
	
	public ProcessEngineContext(){}
	
	public ProcessEngineContext(ExecutionContext executionContext, Param<?> actionParam){
		commandMessage = executionContext.getCommandMessage();
		quadModel = executionContext.getQuadModel();
		this.actionParam = actionParam;
	}
	
	public boolean isOutputAnException() {
		return output != null && output instanceof Exception;
	}
	
	public Command getCommand(){
		return commandMessage.getCommand();
	}
	
	public boolean isContainsAlias(String aliasType, String alias) {
		List<CommandElement> elementsWithAliasType = new ArrayList<>();
		traverseElems(getCommandMessage().getCommand().root(), elementsWithAliasType, Type.findByDesc(aliasType));
		CommandElement cmdElem = elementsWithAliasType.stream()
							.filter((e) -> e.getAlias().equalsIgnoreCase(alias))
							.findFirst()
							.orElse(null);
		if(cmdElem != null) {
			return true;
		}
		return false;
	}
	
	public String getProcessAlias(){
		String processAlias = getCommandMessage().getCommand().getElement(Type.ProcessAlias).map(e->e.getAlias()).orElse(null);
		return processAlias;
	}	
	
	private void traverseElems(CommandElementLinked linked, List<CommandElement> elementsWithAliasType, Type aliasType) {
		if(linked.getType() == aliasType) {
			elementsWithAliasType.add(linked);
		}
		if(linked.hasNext()) {
			traverseElems(linked.next(), elementsWithAliasType, aliasType);
		}
	}	
	
}
