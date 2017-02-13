package com.anthem.oss.nimbus.core;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DataIntegrityViolationExecption extends FrameworkRuntimeException{
	
	private static final long serialVersionUID = 1L;

	private Class<?> clazz;
	
	
    public DataIntegrityViolationExecption(Class<?> clazz) {
    	super();
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Class<?> clazz) {
    	super(message);
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Throwable cause, Class<?> clazz) {
    	super(message, cause);
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> clazz) {
    	super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }
    
}
