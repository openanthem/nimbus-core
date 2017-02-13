/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

/**
 * @author AC67870
 *
 */
public class ServiceActivatorException extends FrameworkRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceActivatorException() {
	}

	public ServiceActivatorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServiceActivatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceActivatorException(String message) {
		super(message);
	}

	public ServiceActivatorException(Throwable cause) {
		super(cause);
	}
	
	

}
