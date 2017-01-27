/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandBuilder;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.command.ExecuteOutput.BehaviorExecute;
import com.anthem.nimbus.platform.spec.model.command.MultiExecuteOutput;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Behavior;
import com.anthem.nimbus.platform.utils.converter.CommandMessageConverter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component("defaultExpressionHelper")
public class DefaultExpressionHelper extends AbstractExpressionHelper{
	
	@Autowired CommandMessageConverter converter;
	
	/**
	 * 
	 * @param cmdMsg
	 * @param execution
	 * @param resolvedUri
	 * @param requestParameter
	 * @param args
	 * @return
	 */
	final public Object _get(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._get);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		Object obj = executeProcess(coreCmdMsg);
		
		if(obj instanceof MultiExecuteOutput){
			return ((Map<Integer, BehaviorExecute<?>>) ((MultiExecuteOutput) obj).getResult()).get(0).getResult();
		}
		return obj;
	}
	
	/**
	 * 
	 * @param cmdMsg
	 * @param execution
	 * @param resolvedUri
	 * @param args
	 * @return
	 */
	final public Object _update(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._update);
		command.templateBehaviors().add(Behavior.$execute);
		String payload = converter.convert((String)args[0]);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(payload);
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}
	
	final public Object _search(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._search);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload((String)args[0]);
		Object obj =  executeProcess(coreCmdMsg);
		return obj;
	}
	
	final public Object _new(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._new);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}
	
	final public Object _delete(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._delete);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}
	
}
