/**
 *  Copyright 2016-2018 the original author or authors.
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidArgumentException;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.defn.Constants;

/**
 * @author Rakesh Patel
 *
 */
public class RemoteModelClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
	private static final String BEHAVIOR_KEY = Constants.MARKER_URI_BEHAVIOR.code+Constants.PARAM_ASSIGNMENT_MARKER.code;
	private static final String BEHAVIOR_EXECUTE = Constants.MARKER_URI_BEHAVIOR.code+Constants.PARAM_ASSIGNMENT_MARKER.code+Behavior.$execute.name();

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		
		return execution.execute(new OverrideHttpRequestWrapper(request), body);
	}
	
	
	private class OverrideHttpRequestWrapper extends HttpRequestWrapper {

		public OverrideHttpRequestWrapper(HttpRequest request) {
			super(request);
		}
		
		@Override
		public URI getURI() {
			URI uri = super.getURI();
			
			String commandUri = uri.toString();
			
			//Override behavior to $execute for remote model ws calls
			int indexOf = StringUtils.indexOf(commandUri, BEHAVIOR_KEY);
			if(indexOf != -1) {
				int startIndex = StringUtils.indexOf(commandUri, BEHAVIOR_KEY);
				int endIndex = StringUtils.indexOf(commandUri, Constants.REQUEST_PARAMETER_DELIMITER.code, startIndex);
				
				String behaviorToReplace = "";
				
				if(endIndex == -1) 
					behaviorToReplace = StringUtils.substring(commandUri, startIndex);
				else
					behaviorToReplace = StringUtils.substring(commandUri, startIndex, endIndex);
				
				commandUri = StringUtils.replace(commandUri, behaviorToReplace, BEHAVIOR_EXECUTE);
			}
			
			try {
				return new URI(commandUri);
			} catch (URISyntaxException e) {
				throw new FrameworkRuntimeException("Could not override behaviors in remote model's ws uri: "+uri, e);
			}
			
		}

		@Override
		public HttpHeaders getHeaders() {
			HttpHeaders headers = super.getHeaders();
			headers.add("responseBody", "_raw");
			return headers;
		}
	}

}
