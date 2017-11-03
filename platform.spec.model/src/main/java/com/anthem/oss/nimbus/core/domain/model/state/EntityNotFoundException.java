package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class EntityNotFoundException extends FrameworkRuntimeException {
	
    private static final long serialVersionUID = 1L;
	
    private Class<?> clazz;
    

    public EntityNotFoundException(Class<?> clazz) {
    	super();
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Class<?> clazz) {
    	super(message);
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Throwable cause, Class<?> clazz) {
    	super(message, cause);
        this.clazz = clazz;
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> clazz) {
    	super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }
    
}
