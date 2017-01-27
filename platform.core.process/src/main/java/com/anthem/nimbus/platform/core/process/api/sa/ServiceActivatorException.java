/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.sa;

import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

/**
 * @author AC67870
 *
 */
public class ServiceActivatorException extends PlatformRuntimeException {

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
