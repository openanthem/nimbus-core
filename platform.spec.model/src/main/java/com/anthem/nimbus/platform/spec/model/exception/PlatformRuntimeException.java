/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.exception;

import java.util.UUID;

import com.anthem.nimbus.platform.spec.model.command.ExecuteError;

/**
 * @author Soham Chakravarti
 *
 */
public class PlatformRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    final private ExecuteError execEx;
    

	public PlatformRuntimeException() {
		this.execEx = create(null);
	}
	
    public PlatformRuntimeException(String message) {
		super(message);
		this.execEx = create(message);
	}
	
    public PlatformRuntimeException(Throwable cause) {
		super(cause);
		this.execEx = create(null);
	}
	
    public PlatformRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.execEx = create(message);
	}

	public PlatformRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.execEx = create(message);
	}
	
	
    private ExecuteError create(String msg) {
		return new ExecuteError(UUID.randomUUID().toString(), this.getClass(), msg);
	}

    @Override
	public String getMessage() {
		return constructMessage(super.getMessage());
	}
	
    @Override
	public String getLocalizedMessage() {
		return constructMessage(super.getLocalizedMessage());
	}
	
    private String constructMessage(String embedMsg) {
		return new StringBuilder().append("[Ex-UID:").append(execEx.getUniqueId()).append("] ").append(embedMsg).toString();
	}
	
    public ExecuteError getExecuteError() {
		return execEx;
	}
	
}
