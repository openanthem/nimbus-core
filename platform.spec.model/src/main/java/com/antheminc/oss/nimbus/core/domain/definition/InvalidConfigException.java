/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;

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
