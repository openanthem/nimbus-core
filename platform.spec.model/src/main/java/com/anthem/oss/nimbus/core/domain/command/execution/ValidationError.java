/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soham Chakravarti
 *
 */
@Data
public abstract class ValidationError {

	private String modelAlias;
	
	private String code;
	
	private String msg;
	
	
	
	@Data @EqualsAndHashCode(callSuper=false)
	public static class Param extends ValidationError {
	    private String paramCode;
	}
	
	
	
	@Data @EqualsAndHashCode(callSuper=false)
	public static class Model extends ValidationError {
		private List<String> paramCodes;
	}
	
}
