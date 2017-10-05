package com.anthem.oss.nimbus.core.session.impl;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.util.JustLogit;

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
