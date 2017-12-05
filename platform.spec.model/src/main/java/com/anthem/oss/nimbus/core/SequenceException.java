package com.antheminc.oss.nimbus.core;

/**
 * @author Rakesh Patel
 *
 */
public class SequenceException extends FrameworkRuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public SequenceException() { }

    public SequenceException(String message) {
		super(message);
	}
    
    public SequenceException(Throwable cause) {
		super(cause);
	}

    public SequenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SequenceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
