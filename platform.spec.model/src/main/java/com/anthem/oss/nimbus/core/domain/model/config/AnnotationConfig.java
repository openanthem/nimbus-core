/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private String name;
	
	private String value;
	
	@JsonIgnore
	private Annotation annotation;
	
	Map<String, Object> attributes;
	
}
