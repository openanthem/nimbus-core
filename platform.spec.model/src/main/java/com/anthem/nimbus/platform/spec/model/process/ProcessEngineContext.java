/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.CommandMessage;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ProcessEngineContext implements Serializable {
    
	private static final long serialVersionUID = 1L;
	

	private CommandMessage commandMsg;

	private transient Object output;

	private transient Object input;
	
	
	public boolean isOutputAnException() {
		return output != null && output instanceof Exception;
	}
	
}
