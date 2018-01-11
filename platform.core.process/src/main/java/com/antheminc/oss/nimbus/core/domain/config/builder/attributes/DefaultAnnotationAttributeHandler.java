package com.antheminc.oss.nimbus.core.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>Default Implementation of <tt>AnnotationAttributeHandler</tt> that simply returns a key/value 
 * map of the annotation attributes defined in the <tt>annotation</tt> element with no
 * additional changes made.
 * 
 * @author Tony Lopez (AF42192)
 * @see com.antheminc.oss.nimbus.core.domain.config.builder.attributes.AnnotationAttributeHandler
 *
 */
public class DefaultAnnotationAttributeHandler implements AnnotationAttributeHandler {

	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.domain.config.builder.AnnotationAttributeHandler#generateFrom(java.lang.reflect.AnnotatedElement, java.lang.annotation.Annotation)
	 */
	@Override
	public Map<String, Object> generateFrom(AnnotatedElement annotatedElement, Annotation annotation) {
		final AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotatedElement, annotation, false, true);
		final HashMap<String, Object> map = new HashMap<>();
		
		for(final Entry<String, Object> entry: annotationAttributes.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

}
