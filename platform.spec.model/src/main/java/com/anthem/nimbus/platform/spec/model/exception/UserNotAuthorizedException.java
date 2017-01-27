/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.exception;

/**
 * @author Swetha Vemuri
 *
 */
public class UserNotAuthorizedException extends PlatformRuntimeException{
	
	private static final long serialVersionUID = 1L;	

	public UserNotAuthorizedException() { }

    public UserNotAuthorizedException(String message) {
		super(message);
	}
    
    public UserNotAuthorizedException(Throwable cause) {
		super(cause);
	}

    public UserNotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotAuthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
