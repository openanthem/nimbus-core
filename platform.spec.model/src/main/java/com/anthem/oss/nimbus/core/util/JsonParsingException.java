package com.anthem.oss.nimbus.core.util;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class JsonParsingException extends FrameworkRuntimeException {
	private static final long serialVersionUID = 1L;

	public JsonParsingException() { }

    public JsonParsingException(String message) {
        super(message);
    }

    public JsonParsingException(Throwable cause) {
        super(cause);
    }

    public JsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
