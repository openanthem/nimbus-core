/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.binder.AssignmentTask.TaskStatus;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput.BehaviorExecute;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component("defaultExpressionHelper")
public class DefaultExpressionHelper extends AbstractExpressionHelper{
	
	@Autowired CommandMessageConverter converter;
	
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
	
	final public void _setExternal(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		coreCmdMsg.setCommand(command);
		if(args.length > 1)
			coreCmdMsg.setRawPayload((String)args[1]);
		MultiExecuteOutput obj =  (MultiExecuteOutput)executeProcess(coreCmdMsg);
		QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		StringBuilder targetParamPath = new StringBuilder((String)args[0]);
		targetParamPath.append(".m");
		quadModel.getView().findModelByPath(targetParamPath.toString()).setState(obj.getSingleResult());
	}
	
	
	final public void _execute(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		coreCmdMsg.setCommand(command);
		if(args.length > 1)
			coreCmdMsg.setRawPayload((String)args[1]);
		executeProcess(coreCmdMsg);
	}	
	
	final public Model<?> _getCore(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		return quadModel.getCore();
	}		
	
	
	final public Object _concat(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		StringBuilder builder = new StringBuilder();
		for(Object arg: args){
			builder.append(arg.toString());
		} 
		return builder.toString();
	}
	
	
	final public void _createSyncTask(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._new);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		executeProcess(coreCmdMsg);
		QuadModel<?, ?> taskQuadModel = UserEndpointSession.getOrThrowEx(coreCmdMsg.getCommand());
		QuadModel<?, ?> entityQuadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		taskQuadModel.getCore().findStateByPath("/internalStatus").setState(TaskStatus.OPEN);
		taskQuadModel.getCore().findStateByPath("/entity").setState(entityQuadModel.getCore().getState());
	}	
	
	
	
}
