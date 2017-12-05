/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class CommandMessage implements Serializable {
	
	public static final String EMPTY_JSON_REGEX = "(^\\{\\s*\\}$)";

	private static final long serialVersionUID = 1L;
	
	private Command command;
	
	private String rawPayload;

	public CommandMessage() {
	
	}
	
	public CommandMessage(Command command, String rawPayload) {
		setCommand(command);
		setRawPayload(rawPayload);
	}
	
	public boolean hasPayload() {
		return StringUtils.trimToNull(getRawPayload()) != null && !Pattern.matches(EMPTY_JSON_REGEX, getRawPayload());
	}
	
	@Override
	public CommandMessage clone() {
		CommandMessage cloned = new CommandMessage();
		cloned.setCommand(getCommand().clone());
		cloned.setRawPayload(getRawPayload());
		return cloned;
	}
	
}
