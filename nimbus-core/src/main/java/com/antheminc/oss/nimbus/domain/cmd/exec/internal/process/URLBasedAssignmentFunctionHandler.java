/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 * @author Rakesh Patel
 *
 */

@Getter
abstract public class URLBasedAssignmentFunctionHandler<T,R,S> implements FunctionHandler<T,R> {
	
	@Autowired
	private CommandExecutorGateway executorGateway; 
	
	@SuppressWarnings("unchecked")
	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		S state = null;
		//TODO - Expose 2 other flavors for _set. 1. Set by value 2. Set by executing rule file.
		//TODO - When we set by value, if the value is something like Status.INACTIVE, have to use querydsl replace or come up with some other approach
		Param<S> targetParameter = findTargetParam(executionContext);
		String value = executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.REQUEST_PARAMETER_VALUE_MARKER.code);
		if (!StringUtils.isBlank(value)) {
			state = (S) value;
		} else {
			state = isInternal(executionContext.getCommandMessage()) ? getInternalState(executionContext): getExternalState(executionContext);
		}
		return assign(executionContext,actionParameter,targetParameter,state);
	}
	
	abstract public R assign(ExecutionContext executionContext, Param<T> actionParameter,Param<S> targetParameter, S state);

	protected String getUrl(CommandMessage commandMessage){
		
		List<String> queryParamsToInclude = getQueryParamsToInclude();
		
		StringBuilder url = new StringBuilder();
		url.append(commandMessage.getCommand().getFirstParameterValue(Constants.REQUEST_PARAMETER_URL_MARKER.code));
		
		commandMessage.getCommand().getRequestParams().entrySet().stream()
					.filter(map -> !Constants.REQUEST_PARAMETER_URL_MARKER.code.equalsIgnoreCase(map.getKey()))
					.filter(map -> queryParamsToInclude.contains(map.getKey()))
					.filter(map -> map.getValue() != null && map.getValue().length > 0)
					.forEach(map -> url.append(Constants.REQUEST_PARAMETER_DELIMITER.code).append(map.getKey()).append(Constants.PARAM_ASSIGNMENT_MARKER.code).append(map.getValue()[0]));
		
		return url.toString();
	}
	
	protected String getUrlStripParams(CommandMessage commandMessage){
		
		StringBuilder url = new StringBuilder();
		url.append(commandMessage.getCommand().getFirstParameterValue(Constants.REQUEST_PARAMETER_URL_MARKER.code));
		return url.toString();
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
		String url = commandMessage.getCommand().getFirstParameterValue(Constants.REQUEST_PARAMETER_URL_MARKER.code);
		if(StringUtils.startsWith(url, Constants.SEGMENT_PLATFORM_MARKER.code)) {
			return false;
		}
		return true;
	}
	
	protected Param<S> findTargetParam(ExecutionContext context){
		String parameterPath = context.getCommandMessage().getCommand().getAbsoluteDomainAlias();
		return context.getRootModel().findParamByPath(parameterPath);
	}	
	
	protected S getInternalState(ExecutionContext executionContext){
		String url = getUrlStripParams(executionContext.getCommandMessage());
		return executionContext.findStateByPath(url);
		
	}
	
	protected S getExternalState(ExecutionContext executionContext){
		CommandMessage commandToExecute = buildExternalCommand(executionContext.getCommandMessage());
		
		MultiOutput response = getExecutorGateway().execute(commandToExecute);
		//TODO Soham: temp fix, need to talk to Jayant
		return (S)response.getOutputs().get(0).getValue();
	}
	
	// TODO - move this to config lookup
	private List<String> getQueryParamsToInclude() {
		return Arrays.asList(Constants.SEARCH_REQ_WHERE_MARKER.code,Constants.SEARCH_REQ_ORDERBY_MARKER.code, 
				Constants.SEARCH_REQ_FETCH_MARKER.code,Constants.SEARCH_REQ_AGGREGATE_MARKER.code,
				Constants.SEARCH_REQ_PAGINATION_SIZE.code,Constants.SEARCH_REQ_PAGINATION_PAGE_NUM.code, 
				Constants.SEARCH_REQ_PAGINATION_SORT_PROPERTY.code,
				Constants.SEARCH_REQ_PROJECT_MAPPING_MARKER.code);
	}

}
