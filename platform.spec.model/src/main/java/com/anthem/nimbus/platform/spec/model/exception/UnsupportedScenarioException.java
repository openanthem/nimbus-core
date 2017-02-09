/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.exception;

/**
 * @author Soham Chakravarti
 *
 */
public class UnsupportedScenarioException extends PlatformRuntimeException {

	private static final long serialVersionUID = 1L;

	public UnsupportedScenarioException() {}

	public UnsupportedScenarioException(String message) {
		super(message);
	}

	public UnsupportedScenarioException(Throwable cause) {
		super(cause);
	}

	public UnsupportedScenarioException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedScenarioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
