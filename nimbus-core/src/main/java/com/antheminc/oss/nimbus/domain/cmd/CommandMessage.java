/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.cmd;

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
	
	public CommandMessage(CommandMessage source) {
		setCommand(new Command(source.getCommand()));
		setRawPayload(source.getRawPayload());
	}
	
}
