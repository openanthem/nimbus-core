package com.anthem.nimbus.platform.spec.model.exception;

import com.anthem.nimbus.platform.spec.model.Model;

import lombok.Getter;

/**
 * 
 * @author 
 *
 */
@Getter
public class DataIntegrityViolationExecption extends PlatformRuntimeException{
	
	private static final long serialVersionUID = 1L;

	private Class<? extends Model> clazz;
	
	
    public DataIntegrityViolationExecption(Class<? extends Model> clazz) {
    	super();
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Class<? extends Model> clazz) {
    	super(message);
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Throwable cause, Class<? extends Model> clazz) {
    	super(message, cause);
        this.clazz = clazz;
    }

    public DataIntegrityViolationExecption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<? extends Model> clazz) {
    	super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }
    
//    @Override
//    public String getMessage(){
//   // 	System.out.println("Data int ex"+super.getMessage()+"super"+super.toString());
//        return this.getMessage();
//    }
    
}
