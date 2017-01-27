package com.anthem.nimbus.platform.spec.model.exception;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class JSonParsingException extends PlatformRuntimeException {
    public JSonParsingException() { }

    public JSonParsingException(String message) {
        super(message);
    }

    public JSonParsingException(Throwable cause) {
        super(cause);
    }

    public JSonParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSonParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
