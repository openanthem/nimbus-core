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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import com.antheminc.oss.nimbus.domain.defn.Constants;

/**
 * @author Rakesh Patel
 *
 */
public class RemoteModelClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
	
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
		public HttpHeaders getHeaders() {
			HttpHeaders headers = super.getHeaders();
			headers.add(Constants.HTTP_RESPONSEBODY_INTERCEPTOR_HEADER.code, Constants.HTTP_RESPONSEBODY_INTERCEPTOR_HEADER_RAW.code);
			return headers;
		}
	}

}
