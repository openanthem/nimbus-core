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

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSessionIdLoggerInterceptor extends HandlerInterceptorAdapter {
	
	private static JustLogit logIt = new JustLogit(WebSessionIdLoggerInterceptor.class);
	
	public static final String KEY_SESSION_ID = "SESSIONID";
	private static final String KEY_SESSION_NOT_FOUND = "SESSION-NOT-FOUND";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception {
		addSessionIdIfAny();
		
		return true;
	}
	
	public static void addSessionIdIfAny() {
		try {
			String sessionId = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
				.map(RequestAttributes::getSessionId)
				.orElse(KEY_SESSION_NOT_FOUND);
			
			MDC.put(KEY_SESSION_ID, sessionId);
			
		} catch (Exception ex) {
			MDC.put(KEY_SESSION_ID, KEY_SESSION_NOT_FOUND);
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		clearSessionIdIfAny();
		
	}
	
	public static void clearSessionIdIfAny() {
		try {
			MDC.remove(KEY_SESSION_ID);
		}
		catch (Exception ex) {
			logIt.warn(() ->"Failed to clear SESSIONID from MDC", ex);
		}
	}
	
}
