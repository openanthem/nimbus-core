package com.anthem.nimbus.core.authorization;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

/**
 * @author AC67870
 *
 */
public class AuthorizationException extends FrameworkRuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}
	
	

}
