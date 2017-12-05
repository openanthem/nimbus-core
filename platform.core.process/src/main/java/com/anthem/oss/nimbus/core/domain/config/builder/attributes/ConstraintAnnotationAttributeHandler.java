package com.antheminc.oss.nimbus.core.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	public static final String JSR_DEFAULT_MESSAGE_REGEX = "\\{javax.validation.constraints.(.*).message\\}";
	
	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.domain.config.builder.AnnotationAttributeHandler#generateFrom(java.lang.reflect.AnnotatedElement, java.lang.annotation.Annotation)
	 */
	@Override
	public Map<String, Object> generateFrom(AnnotatedElement annotatedElement, Annotation annotation) {
		final AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotatedElement, annotation, false, true);
		final HashMap<String, Object> map = new HashMap<>();
		
		for(final Entry<String, Object> entry: annotationAttributes.entrySet()) {
			// Prefer empty-string over JSR defaults.
			if (entry.getKey().equals(ATTRIBUTE_MESSAGE_NAME) && ((String) entry.getValue()).matches(JSR_DEFAULT_MESSAGE_REGEX)) {
				map.put(entry.getKey(), ATTRIBUTE_MESSAGE_VALUE_DEFAULT);
			} else {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

}
