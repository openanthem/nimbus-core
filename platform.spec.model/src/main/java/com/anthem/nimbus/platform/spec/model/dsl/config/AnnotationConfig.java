/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class AnnotationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	String name;
	
	private String value;
	
	Map<String, Object> attributes;
	
}
