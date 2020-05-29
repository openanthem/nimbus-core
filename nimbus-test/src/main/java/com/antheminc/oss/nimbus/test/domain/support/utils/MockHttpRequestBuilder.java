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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.defn.Constants;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public final class MockHttpRequestBuilder {

	private final MockHttpServletRequest httpReq;
	
	public MockHttpServletRequest getMock() {
		return httpReq;
	}
	
	public static MockHttpRequestBuilder withUri(String uri) {
		return withUri(HttpMethod.GET, uri);
	}
	
	public static MockHttpRequestBuilder withUri(HttpMethod httpMethod, String uri) {
		return new MockHttpRequestBuilder(new MockHttpServletRequest(httpMethod.name(), uri));
	}
	
	public MockHttpRequestBuilder addRefId(Serializable refId) {
		String uri = httpReq.getRequestURI() + ":" + refId;
		httpReq.setRequestURI(uri);
		
		return this;
	}

	public MockHttpRequestBuilder addRefMap(Map<String, String> refMap) {
		StringBuilder uri = new StringBuilder(httpReq.getRequestURI() + ":");
		
		refMap.entrySet().stream()
			.forEach(e->{
				String k = e.getKey();
				String v = e.getValue();
				uri.append(k).append("=").append(v).append("&");
			});
		
		String finalUri = uri.toString();
		finalUri = finalUri.substring(0, finalUri.length()-1); 
		
		httpReq.setRequestURI(finalUri);
		
		return this;
	}
	
	public MockHttpRequestBuilder addNested(String nestedPath) {
		String uri = httpReq.getRequestURI() + nestedPath;
		httpReq.setRequestURI(uri);
		
		return this;
	}

	
	public MockHttpRequestBuilder addAction(Action a) {
		String uri = httpReq.getRequestURI() + "/" + a.name();
		httpReq.setRequestURI(uri);
		
		return this;
	}
	
	public MockHttpRequestBuilder addBehavior(Behavior b) {
		httpReq.addParameter(Constants.MARKER_URI_BEHAVIOR.code, b.name());
		
		return this;
	}
	
	public MockHttpRequestBuilder addParam(String name, Serializable value) {
		httpReq.addParameter(name, value != null? value.toString(): null);
		return this;
	}
}
