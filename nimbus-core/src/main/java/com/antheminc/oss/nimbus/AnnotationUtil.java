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
package com.antheminc.oss.nimbus;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>Utility class for support for common annotation access in the Framework.
 * 
 * @since 1.1
 * @author Tony Lopez
 *
 */
public class AnnotationUtil {

	private AnnotationUtil() {
	}

	/**
	 * <p>Safely retrieve an value from the provided {@code annotation}.
	 * @throws InvalidConfigException if the attribute for {@code name} is not
	 *             defined on {@code annotation} or the annotation type is not
	 *             matching the expected return value.
	 * @param annotation the annotation to inspect
	 * @param name the attribute (by name) to retrieve
	 * @param clazz the expected type of the return value
	 * @return the value of the attribute
	 */
	public static <T> T safelyRetrieveAnnotationAttribute(Annotation annotation, String name, Class<T> clazz) {
		Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
		if (!annotationAttributes.containsKey(name)) {
			throw new InvalidConfigException("The \"" + name + "\" attribute was not found within " + annotation
					+ ". The attribute \"" + name + "\" must be defined on the annotaton type "
					+ annotation.getClass().getSimpleName() + ".");
		}
		Object oTargetPath = annotationAttributes.get(name);
		if (null == oTargetPath) {
			throw new InvalidConfigException("The \"" + name + "\" attribute was not found within " + annotation
					+ ". The attribute \"" + name + "\" must not be null.");
		}
		if (!clazz.isInstance(oTargetPath)) {
			throw new InvalidConfigException(
					"The \"" + name + "\" attribute of " + annotation + " must be of type java.lang.String.");
		}
		return (T) annotationAttributes.get(name);
	}
}
