/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ProcessResponse {

	private Object response;

	private String executionId;
	
	private String definitionId;
	
}
