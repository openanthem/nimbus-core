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
package com.anthem.oss.nimbus.core.session.impl;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public class HttpSessionCache implements SessionCache {

	private JustLogit logit = new JustLogit();
	
	@Override
	public void put(String id, ExecutionContext eCtx) {
		getRequestAttributes().setAttribute(id, eCtx, RequestAttributes.SCOPE_SESSION);		
	}

	@Override
	public ExecutionContext get(String id) {
		return (ExecutionContext) getRequestAttributes().getAttribute(id, RequestAttributes.SCOPE_SESSION);
	}

	@Override
	public boolean remove(String id) {
		try {
			getRequestAttributes().removeAttribute(id, RequestAttributes.SCOPE_SESSION);
			return true;
		} catch (Exception e) {
			logit.error(() -> "Failed to remove object[id=" + id + "] from session cache.", e);
			return false;
		}
	}

	@Override
	public boolean clear() {
		
		final String[] keys = getRequestAttributes().getAttributeNames(RequestAttributes.SCOPE_SESSION);
		
		try {
			for(final String key : keys) {	
				final ExecutionContext eCtx = get(key);
				eCtx.getQuadModel().getRoot().getExecutionRuntime().stop();
			}
		} catch (Exception e) {
			logit.error(() -> "Failed to clear session cache.", e);
			return false;
		}
		
		// clear the session, if no failure present.
		for(final String key : keys) {
			remove(key);
		}
		
		return true;
	}

	private RequestAttributes getRequestAttributes() {
		return RequestContextHolder.getRequestAttributes();		
	}
}
