package com.anthem.oss.nimbus.core.domain.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Implementation of <tt>AnnotationAttributeHandler</tt> that ensures all JSR <tt>Constraint</tt>
 * attributes are filtered as needed.
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public class ConstraintAnnotationAttributeHandler implements AnnotationAttributeHandler {

	public static final String ATTRIBUTE_MESSAGE_NAME = "message";
	public static final String ATTRIBUTE_MESSAGE_VALUE_DEFAULT = "";
	
	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.config.builder.AnnotationAttributeHandler#generateFrom(java.lang.reflect.AnnotatedElement, java.lang.annotation.Annotation)
	 */
	@Override
	public Map<String, Object> generateFrom(AnnotatedElement annotatedElement, Annotation annotation) {
		final AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotatedElement, annotation, false, true);
		final HashMap<String, Object> map = new HashMap<>();
		
		for(String annotationAttribute: annotationAttributes.keySet()) {
			// Prefer empty-string over JSR defaults.
			if (annotationAttribute.equals(ATTRIBUTE_MESSAGE_NAME) && 
					((String) annotationAttributes.get(annotationAttribute)).matches("\\{javax.validation.constraints.(.*).message\\}")) {
				map.put(annotationAttribute, ATTRIBUTE_MESSAGE_VALUE_DEFAULT);
			} else {
				map.put(annotationAttribute, annotationAttributes.get(annotationAttribute));
			}
		}
		return map;
	}

}
