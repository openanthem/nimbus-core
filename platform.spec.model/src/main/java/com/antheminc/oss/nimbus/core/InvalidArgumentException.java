/**
 * 
 */
package com.antheminc.oss.nimbus.core;

/**
 * @author Soham Chakravarti
 *
 */
public class InvalidArgumentException extends FrameworkRuntimeException {

	private static final long serialVersionUID = 1L;

	
	public InvalidArgumentException() { }

	public InvalidArgumentException(String message) {
		super(message);
	}

	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}

	public InvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
