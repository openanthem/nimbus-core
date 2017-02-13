/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.process;

import com.anthem.oss.nimbus.core.domain.Constants;

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
