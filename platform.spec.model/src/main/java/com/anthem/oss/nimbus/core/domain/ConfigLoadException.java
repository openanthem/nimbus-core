/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

/**
 * @author Soham Chakravarti
 *
 */
public class ConfigLoadException extends FrameworkRuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigLoadException() {}

	public ConfigLoadException(String message) {
		super(message);
	}

	public ConfigLoadException(Throwable cause) {
		super(cause);
	}

	public ConfigLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
