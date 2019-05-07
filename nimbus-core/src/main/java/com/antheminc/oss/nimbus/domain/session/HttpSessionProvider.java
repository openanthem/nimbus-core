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
package com.antheminc.oss.nimbus.domain.session;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Rakesh Patel
 *
 */
public class HttpSessionProvider extends AbstractSessionProvider {

	public String getSessionId() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes) {
			return null;
		}
		return requestAttributes.getSessionId();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R> R getAttribute(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes) {
			return null;
		}
		return (R)requestAttributes.getAttribute(key, RequestAttributes.SCOPE_SESSION);
	}

	@Override
	public void setAttribute(String key, Object value) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes) {
			return;
		}
		requestAttributes.setAttribute(key, value, RequestAttributes.SCOPE_SESSION);
	}
	
	@Override
	public boolean removeAttribute(String key) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (null == requestAttributes) {
			return false;
		}
		if(requestAttributes.getAttribute(key,RequestAttributes.SCOPE_SESSION) == null)
			return false;
		requestAttributes.removeAttribute(key, RequestAttributes.SCOPE_SESSION);
		return true;
	}
	
}
