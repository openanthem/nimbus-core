/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.config.builder.AnnotationAttributeHandler;

/**
 * Implementation of <tt>AnnotationAttributeHandler</tt> that ensures all JSR <tt>Constraint</tt>
 * attributes are filtered as needed.
 * 
 * @author Tony Lopez
 *
 */
public class ConstraintAnnotationAttributeHandler implements AnnotationAttributeHandler {

	public static final String ATTRIBUTE_MESSAGE_NAME = "message";
	public static final String ATTRIBUTE_MESSAGE_VALUE_DEFAULT = "";
	public static final String JSR_DEFAULT_MESSAGE_REGEX = "\\{javax.validation.constraints.(.*).message\\}";
	
	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.config.builder.AnnotationAttributeHandler#generateFrom(java.lang.reflect.AnnotatedElement, java.lang.annotation.Annotation)
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
