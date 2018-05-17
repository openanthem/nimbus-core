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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Soham Chakravarti
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSessionIdLoggerFilter implements Filter {

	public static final String KEY_SESSION_ID = "SESSIONID";
	private static final String KEY_SESSION_NOT_FOUND = "SESSION-NOT-FOUND";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {

		String sessionId = getSessionId();
		MDC.put(KEY_SESSION_ID, sessionId);
		
		chain.doFilter(request, response);
	}

	
	private String getSessionId() {
		try {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if(requestAttributes != null) 
				return requestAttributes.getSessionId();
			
			return KEY_SESSION_NOT_FOUND;
			
		} catch (Exception ex) {
			return KEY_SESSION_NOT_FOUND;
		}
	}
	
	@Override
	public void destroy() {}

}
