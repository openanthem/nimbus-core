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
package com.antheminc.oss.nimbus.channel.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class WebCommandBuilder {

	private JustLogit logit = new JustLogit(this.getClass());
	
	public Command build(HttpServletRequest request) {
		final String requestUri = constructRequestUri(request);
		logit.trace(()->"Received http request. "+request.getMethod()+" URI: "+requestUri);
		
		return handleInternal(requestUri, request.getParameterMap());
	}
	
	public Command build(HttpServletRequest request, ModelEvent<String> event) {
		String uri = constructRequestUri(request);
		logit.info(()->"Received http request. "+request.getMethod()+" URI: "+uri+" with event: "+event);
		
		final String constructedUri;
		if(event==null) {
			constructedUri = constructRequestUri(request);
		} else {
			String clientUri = StringUtils.substringBefore(uri, Constants.SEPARATOR_URI_PLATFORM.code+Constants.SEPARATOR_URI.code); //separator = /p/
			constructedUri = clientUri + Constants.SEPARATOR_URI_PLATFORM.code + event.getPath() + Constants.SEPARATOR_URI.code +event.getType();
			
			logit.info(()->"Constructed URI: "+constructedUri);
		}
		
		
		Command cmd = handleInternal(constructedUri, request.getParameterMap());
		return cmd;
	}
	
	public Command handleInternal(String uri, Map<String, String[]> rParams) {
		Command cmd = CommandBuilder.withUri(uri).addParams(rParams).getCommand();
		return cmd;
	} 
	
	private String constructRequestUri(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		if (!StringUtils.isEmpty(request.getContextPath())) {
			requestUri = StringUtils.substringAfter(requestUri, request.getContextPath());
		}
		return requestUri;
	}
	
}
