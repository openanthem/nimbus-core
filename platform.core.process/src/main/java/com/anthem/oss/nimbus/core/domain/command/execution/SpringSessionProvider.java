/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Rakesh Patel
 *
 */
public class SpringSessionProvider implements SessionProvider {

	public String getSessionId() {
		return RequestContextHolder.getRequestAttributes().getSessionId();
	}
}
