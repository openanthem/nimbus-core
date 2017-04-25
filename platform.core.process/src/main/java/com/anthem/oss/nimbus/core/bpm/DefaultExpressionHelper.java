/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import java.util.Optional;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

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

	final public Object _get(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		String domainUri = reconstructWithRefId(cmdMsg, resolvedUri);
		Command command = CommandBuilder.withUri(domainUri.toString()).getCommand();		
		command.setAction(Action._get);
		command.templateBehaviors().add(Behavior.$execute); 		
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(cmdMsg.getRawPayload());
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		return obj.getSingleResult();
	}

	final public Object _update(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._update);
		command.templateBehaviors().add(Behavior.$execute);
		String payload = converter.convert((String) args[0]);
		coreCmdMsg.setCommand(command);
		coreCmdMsg.setRawPayload(payload);
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		return obj.getSingleResult();
	}

	final public Object _search(CommandMessage cmdMsg, DelegateExecution execution, String resolvedUri, Object... args) {
		CommandMessage coreCmdMsg = new CommandMessage();
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		command.setAction(Action._search);
		command.templateBehaviors().add(Behavior.$execute);
		coreCmdMsg.setCommand(command);
		if(args.length > 0) {
			String payload = reconstructWithRefId(cmdMsg, ((String)args[0]));
			coreCmdMsg.setRawPayload(payload);
		}		
		MultiExecuteOutput obj = (MultiExecuteOutput) executeProcess(coreCmdMsg);
		return obj.getSingleResult();
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
		taskQuadModel.getCore().findStateByPath("/internalStatus").setState(TaskStatus.Open);
		taskQuadModel.getCore().findStateByPath("/entity").setState(entityQuadModel.getCore().getState());
	}
	
	/**
	 * Use this method to set the value of the resolved uri path to the internal execution variable passed in the argument
	 * e.g. expression: {@code _setInternal('/patientReferred','patient') } will set the execution variable by name 'patient' to the path 
	 * {@code /patientReferred} of the core model. The execution variable may be coming from the result variable of a previous service task.
	 * 
	 */
	final public void _setInternal(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		QuadModel<?, Object> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		String inputPath = command.getAbsoluteDomainUri();		
		Object obj = execution.getVariable((String)args[0]);
		if(StringUtils.isEmpty(inputPath)){
			quadModel.getCore().setState(obj);
		} else {
			quadModel.getCore().findParamByPath(inputPath).setState(obj);
		}		
	}
	
	/**
	 * Use this expression to fireAllRules for the quadmodel in session
	 * 
	 */
	final public void _fireAllRules(CommandMessage cmdMsg, DelegateExecution execution){
		QuadModel<?, Object> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		quadModel.getCore().fireRules();
		quadModel.getView().fireRules();
	}
	
	/**
	 * Use this expression to fireRules for specific domain mentioned in the resolveduri param.
	 * 
	 */
	final public void _fireRulesByPath(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args){
		QuadModel<?, Object> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		quadModel.getCore().findParamByPath(resolvedUri).fireRules();
	}
	
	final public void _convertAndSet(CommandMessage cmdMsg, DelegateExecution execution, 
			String resolvedUri, Object... args) {
		Command command = CommandBuilder.withUri(resolvedUri.toString()).getCommand();
		String inputPath = command.getAbsoluteDomainUri();
		QuadModel<?, Object> quadModel = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		Assert.notNull(quadModel.getView().findParamByPath(inputPath),"cannot find quadmodel for the path");
		Class<?> targetClass = quadModel.getView().findParamByPath(inputPath).getType().findIfNested().getConfig().getReferredClass();
		Object state = converter.convert(targetClass,cmdMsg.getRawPayload());
		quadModel.getView().findParamByPath(inputPath).setState(state);
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
