/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.task.AssignmentTask.TaskStatus;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultExpressionHelper extends AbstractExpressionHelper {
	
	public static final String PLATFORM_TYPE_ALIAS="#";
	
	CommandMessageConverter converter;
	
	public DefaultExpressionHelper(CommandMessageConverter converter) {
		this.converter = converter;
	}

	final public void _get(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		String domainUri = reconstructWithRefId(cmdMsg, resolvedUri);
		Command command = CommandBuilder.withUri(domainUri.toString()).getCommand();		
		command.setAction(Action._get);
		command.templateBehaviors().add(Behavior.$execute); 		
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		StringBuilder targetParamPath = null;
		if (args.length > 0) {
			if(args[0] != null) {
				targetParamPath = new StringBuilder((String)args[0]);
			}
		}		 
		if(targetParamPath != null && !StringUtils.isEmpty(targetParamPath.toString())) {
			quadModel.getCore().findParamByPath(targetParamPath.toString()).setState(obj.getSingleResult());
		} else {
			quadModel.getCore().setState(obj.getSingleResult());
		}
	}

	final public Object _update(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._update);
		command.templateBehaviors().add(Behavior.$execute);
		String payload = converter.convert((String) args[0]);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(payload);
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}

	final public Object _search(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._search);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		String payload = ((String)args[0]).replaceFirst("#refId", cmdMsg.getCommand().getRefId(Type.ProcessAlias));
		coreCmdMsg.setRawPayload(payload);
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}

	final public void _new(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._new);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		quadModel.getCore().setState(obj.getSingleResult());
	}

	final public Object _delete(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._delete);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		Object obj = executeProcess(coreCmdMsg);
		return obj;
	}

	final public void _setExternal(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		coreCmdMsg.setCommand(command);
		if (args.length > 1) {
			String payload = reconstructWithRefId(cmdMsg, ((String)args[1]));
			coreCmdMsg.setRawPayload(payload);		
		} else {
			coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		}
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		StringBuilder targetParamPath = new StringBuilder((String) args[0]);
		targetParamPath.append(".m");
		quadModel.getView().findModelByPath(targetParamPath.toString()).setState(obj.getSingleResult());
	}

	final public void _setClientUser(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		StringBuilder targetParamPath = new StringBuilder((String) args[0]);
		targetParamPath.append(".m");
		Model<?> viewSAC = quadModel.getView();
		Param<?> p = viewSAC.findParamByPath(targetParamPath.toString());
		System.out.println(p);
		System.out.println(quadModel.getView().findParamByPath(targetParamPath.toString()).getState());

		UserEndpointSession userEndpointSession = (UserEndpointSession) ProcessBeanResolver.appContext
				.getBean(UserEndpointSession.class);

		quadModel.getView().findParamByPath(targetParamPath.toString()).setState(userEndpointSession.getLoggedInUser());

	}

	
	/**
	 * Use this method to set the value of the resolved uri path to the args[0]
	 * e.g. expression: {@code _set('/pageHome/sectionHomeHeader/viewHomeUser.m',userEndpointSession.loggedInUser) } will set the logged in ClientUser to the maps to path 
	 * {@code /pageHome/sectionHomeHeader/viewHomeUser.m} 
	 * 
	 */
	final public void _set(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {

		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		String inputPath = command.getAbsoluteDomainUri();

		quadModel.getView().findParamByPath(inputPath).setState(args[0]);
	}

	final public void _execute(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		coreCmdMsg.setCommand(command);
		if (args.length > 1)
			coreCmdMsg.setRawPayload((String) args[1]);
		executeProcess(coreCmdMsg);
	}

	final public Model<?> _getCore(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		return quadModel.getCore();
	}

	final public Object _concat(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		StringBuilder builder = new StringBuilder();
		for (Object arg : args) {
			builder.append(arg.toString());
		}
		return builder.toString();

	}

	final public void _createSyncTask(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
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
	
	final public void _setInternal(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		coreCmdMsg.setCommand(command);
		command.getRootDomainElement().setRefId(cmdMsg.getCommand().getRefId(Type.ProcessAlias));
		if(args.length > 1)
			coreCmdMsg.setRawPayload((String)args[1]);
		MultiExecuteOutput obj =  (MultiExecuteOutput)executeProcess(coreCmdMsg);
		QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		StringBuilder targetParamPath = new StringBuilder((String)args[0]);
		if(!StringUtils.isEmpty(targetParamPath.toString())) {
			quadModel.getCore().findParamByPath(targetParamPath.toString()).setState(obj.getSingleResult());
		} else {
			quadModel.getCore().setState(obj.getSingleResult());
		}
	}
	
	private String reconstructWithRefId(CommandMessage cmdMsg , String uri) {
		String refIdAlias = StringUtils.substringBetween(uri, PLATFORM_TYPE_ALIAS);
		Type alias = Type.findByDesc(refIdAlias);		
		String refId = cmdMsg.getCommand().getRefId(Optional.ofNullable(alias).orElse(null));
		if(refId != null){
			String resolvedUri = StringUtils.replace(uri, refIdAlias, refId);
			resolvedUri = StringUtils.remove(resolvedUri, PLATFORM_TYPE_ALIAS);
			return resolvedUri;
		}	
		return uri;
	}

}
