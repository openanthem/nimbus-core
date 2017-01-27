/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.exception;

/**
 * @author Soham Chakravarti
 *
 */
public class InvalidConfigException extends PlatformRuntimeException {

	private static final long serialVersionUID = 1L;
	

	public InvalidConfigException() { }

    public InvalidConfigException(String message) {
		super(message);
	}
    
    public InvalidConfigException(Throwable cause) {
		super(cause);
	}

    public InvalidConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConfigException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
