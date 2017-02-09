/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
public class ParamValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	final private Object code;
	
	private String label;
	
	private boolean isActive = true;
	
	
	public ParamValue(Object code) {
		this.code = code;
	}
	
	public ParamValue(Object code, String label) {
		this(code);
		this.label = label;
	}

}
