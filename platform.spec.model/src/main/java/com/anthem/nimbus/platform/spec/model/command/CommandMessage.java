/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class CommandMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private Command command;
	
	private String rawPayload;
	
	
	/**
	 * 
	 */
	@Override
	public CommandMessage clone() {
		CommandMessage cloned = new CommandMessage();
		cloned.setCommand(getCommand().clone());
		cloned.setRawPayload(getRawPayload());
		return cloned;
	}
	
}
