/**
 * 
 */
package com.antheminc.oss.nimbus.core;

/**
 * @author Soham Chakravarti
 *
 */
public class UnsupportedScenarioException extends FrameworkRuntimeException {

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
