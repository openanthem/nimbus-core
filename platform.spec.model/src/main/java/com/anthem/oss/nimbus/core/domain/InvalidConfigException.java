/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

/**
 * @author Soham Chakravarti
 *
 */
public class InvalidConfigException extends FrameworkRuntimeException {

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
