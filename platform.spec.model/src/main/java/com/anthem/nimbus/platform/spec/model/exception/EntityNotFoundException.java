package com.anthem.nimbus.platform.spec.model.exception;

import com.anthem.nimbus.platform.spec.model.Model;

import lombok.Getter;

/**
 * 
 * 
 *
 */
@Getter
public class EntityNotFoundException extends PlatformRuntimeException{
	
    private static final long serialVersionUID = 1L;
	
    private Class<? extends Model> clazz;
    

    public EntityNotFoundException(Class<? extends Model> clazz) {
    	super();
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Class<? extends Model> clazz) {
    	super(message);
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Throwable cause, Class<? extends Model> clazz) {
    	super(message, cause);
        this.clazz = clazz;
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<? extends Model> clazz) {
    	super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }
    
}
