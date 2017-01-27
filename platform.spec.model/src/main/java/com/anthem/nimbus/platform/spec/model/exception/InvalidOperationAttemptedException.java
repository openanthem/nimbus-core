/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.exception;

/**
 * @author Soham Chakravarti
 *
 */
public class InvalidOperationAttemptedException extends PlatformRuntimeException {

	private static final long serialVersionUID = 1L;
    

	public InvalidOperationAttemptedException() { }
	
    public InvalidOperationAttemptedException(String message) {
		super(message);
	}
	
    public InvalidOperationAttemptedException(Throwable cause) {
		super(cause);
	}

    public InvalidOperationAttemptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidOperationAttemptedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
