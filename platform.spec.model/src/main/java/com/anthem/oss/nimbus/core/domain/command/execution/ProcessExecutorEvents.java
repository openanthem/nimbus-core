/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.domain.definition.Constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public enum ProcessExecutorEvents {

    pre(Constants.PREFIX_EVENT_URI.code + "pre"),
	
	post(Constants.PREFIX_EVENT_URI.code + "post"),

	error(Constants.PREFIX_EVENT_URI.code + "error"),
	;
	
	final public String code;
	
}
