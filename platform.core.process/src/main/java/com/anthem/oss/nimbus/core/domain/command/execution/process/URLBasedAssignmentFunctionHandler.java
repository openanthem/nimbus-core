/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
abstract public class URLBasedAssignmentFunctionHandler<T,R,S> implements FunctionHandler<T,R> {
	
	@Autowired
	private CommandExecutorGateway executorGateway; 
	
	@SuppressWarnings("unchecked")
	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		CommandMessage commandFromContext = null;
		S state = null;
		//TODO - Expose 2 other flavors for _set. 1. Set by value 2. Set by executing rule file.
		//TODO - When we set by value, if the value is something like Status.INACTIVE, have to use querydsl replace or come up with some other approach
		Param<S> targetParameter = findTargetParam(executionContext);
		if(StringUtils.isNotBlank(executionContext.getCommandMessage().getCommand().getFirstParameterValue("value"))) {
			commandFromContext =  executionContext.getCommandMessage();
			state = (S) commandFromContext.getCommand().getFirstParameterValue("value");
		} else {
			state = isInternal(executionContext.getCommandMessage()) ? getInternalState(executionContext): getExternalState(executionContext);
		}
		return assign(executionContext,actionParameter,targetParameter,state);
	}
	
	abstract public R assign(ExecutionContext executionContext, Param<T> actionParameter,Param<S> targetParameter, S state);

	//TODO - need to revisit the design of how to pass the where clause in a url. In the below example the _search where clause is being truncated as we are looking at only the url query parameter
	//Ex - /pageOrgUserGroupList/tileUserGroups/sectionUserGroups/userGroupList.m/_process?fn=_set&url=/p/clientusergroup/_search?fn=query&where=clientusergroup.organizationId.eq('<!/.m/id!>')
	protected String getUrl(CommandMessage commandMessage){
		String url = commandMessage.getCommand().getFirstParameterValue("url");
		//Temporarily added the below code. Need to revisit.
		String where = commandMessage.getCommand().getFirstParameterValue("where");
		String orderby = commandMessage.getCommand().getFirstParameterValue("orderby");
		String fetch = commandMessage.getCommand().getFirstParameterValue("fetch");
		String converter = commandMessage.getCommand().getFirstParameterValue("converter");
		String aggregate = commandMessage.getCommand().getFirstParameterValue("aggregate");
		String project = commandMessage.getCommand().getFirstParameterValue("project");
		
		if(StringUtils.isNotBlank(where)) {
			url = url+"&where="+where;
		}
		if(StringUtils.isNotBlank(orderby)) {
			url =  url+"&orderby="+orderby;
		}		
		if(StringUtils.isNotBlank(fetch)) {
			url =  url+"&fetch="+fetch;
		}
		if(StringUtils.isNotBlank(converter)) {
			url =  url+"&converter="+converter;
		}
		if(StringUtils.isNotBlank(aggregate)) {
			url =  url+"&aggregate="+aggregate;
		}
		if(StringUtils.isNotBlank(project)) {
			url =  url+"&project="+project;
		}
		return url;
	}
	
	protected CommandMessage buildExternalCommand(CommandMessage commandMessage){
		String url = getUrl(commandMessage);
		url = commandMessage.getCommand().getRelativeUri(url);
		Command command = CommandBuilder.withUri(url).getCommand();
		
		// TODO Sandeep: decide on which commands should get the payload. Scenario is - we are searching for a form based input. In the below query we are do a member search based on the variable search criteria 
		// Ex - /pageAdvancedMemberSearch/tileAdvancedMemberSearch/sectionMemberSearchResults/patientResult.m/_process?fn=_set&url=/p/patient/_search?fn=example 
		// To decide if we have to resolve the payload and then create the command message
		
		CommandMessage newCommandMessage = new CommandMessage(command, commandMessage.hasPayload() ? commandMessage.getRawPayload() :null);
		return newCommandMessage;
	}
	

	protected boolean isInternal(CommandMessage commandMessage){
		String url = commandMessage.getCommand().getFirstParameterValue("url");
		if(StringUtils.startsWith(url, Constants.SEPARATOR_URI_PLATFORM.code)) {
			return false;
		}
		return true;
	}
	
	protected Param<S> findTargetParam(ExecutionContext context){
		String parameterPath = context.getCommandMessage().getCommand().getAbsoluteDomainAlias();
		return context.getRootModel().findParamByPath(parameterPath);
	}	
	
	protected S getInternalState(ExecutionContext executionContext){
		String url = getUrl(executionContext.getCommandMessage());
		Param<S> sourceParameter = executionContext.getRootModel().findParamByPath(url);
		return sourceParameter.getState();
	}
	
	protected S getExternalState(ExecutionContext executionContext){
		CommandMessage commandToExecute = buildExternalCommand(executionContext.getCommandMessage());
		
		MultiOutput response = executorGateway.execute(commandToExecute);
		//TODO Soham: temp fix, need to talk to Jayant
		return (S)response.getOutputs().get(0).getValue();
	}	

}
