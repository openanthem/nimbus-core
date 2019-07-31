/**
 *  Copyright 2016-2019 the original author or authors.
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
 * <p>Default Implementation of <tt>AnnotationAttributeHandler</tt> that simply returns a key/value 
 * map of the annotation attributes defined in the <tt>annotation</tt> element with no
 * additional changes made.
 * 
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.config.builder.AnnotationAttributeHandler
 *
 */
public class DefaultAnnotationAttributeHandler implements AnnotationAttributeHandler {

	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.config.builder.AnnotationAttributeHandler#generateFrom(java.lang.reflect.AnnotatedElement, java.lang.annotation.Annotation)
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
