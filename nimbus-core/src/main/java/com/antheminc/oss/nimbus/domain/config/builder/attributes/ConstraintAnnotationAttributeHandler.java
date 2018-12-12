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
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationAttributeHandler;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link AnnotationAttributeHandler} that ensures all JSR
 * {@link javax.validation.Constraint} attributes are filtered as needed.
 * 
 * @author Tony Lopez
 *
 */
@Getter
@RequiredArgsConstructor
public class ConstraintAnnotationAttributeHandler implements AnnotationAttributeHandler {

	public static final String ATTRIBUTE_MESSAGE_NAME = "message";
	public static final String ATTRIBUTE_MESSAGE_VALUE_DEFAULT = "";
	public static final String DEFAULT_VALIDATION_MESSAGES_PATH = "validationMessages";
	public static final String JSR_DEFAULT_MESSAGE_REGEX = "\\{javax.validation.constraints.(.*).message\\}";
	public static final String SPEL_CHAR_KEY = "$";
	public static final String REPLACE_REGEX = "\\{(?<messageKey>.*)\\}";
	public static final String ANNOTATION_PREFIX = "@";

	private static final JustLogit LOG = new JustLogit(ConstraintAnnotationAttributeHandler.class);

	private final PropertyResolver propertyResolver;
	private final BeanResolverStrategy beanResolver;
	private final Environment env;
	private final ResourceBundle defaultMessagesBundle;

	private Pattern replacePattern = Pattern.compile(REPLACE_REGEX);

	public ConstraintAnnotationAttributeHandler(BeanResolverStrategy beanResolver) {
		this(beanResolver, DEFAULT_VALIDATION_MESSAGES_PATH);
	}

	public ConstraintAnnotationAttributeHandler(BeanResolverStrategy beanResolver,
			String defaultValidationMessagesPath) {
		this.beanResolver = beanResolver;
		this.propertyResolver = this.beanResolver.get(PropertyResolver.class);
		this.env = this.beanResolver.get(Environment.class);
		this.defaultMessagesBundle = ResourceBundle.getBundle(defaultValidationMessagesPath);
	}

	@Override
	public Map<String, Object> generateFrom(AnnotatedElement annotatedElement, Annotation annotation) {
		final AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotatedElement,
				annotation, false, true);
		final HashMap<String, Object> map = new HashMap<>();

		for (final Entry<String, Object> entry : annotationAttributes.entrySet()) {
			Object resolvedValue = entry.getValue();
			if (entry.getKey().equals(ATTRIBUTE_MESSAGE_NAME)
					&& ((String) entry.getValue()).matches(JSR_DEFAULT_MESSAGE_REGEX)) {
				resolvedValue = this.resolveMessage((String) entry.getValue(), annotatedElement, annotation);
			}
			map.put(entry.getKey(), resolvedValue);
		}
		return map;
	}

	private String resolveMessage(String initialMessageValue, AnnotatedElement annotatedElement,
			Annotation annotation) {
		String key = this.replacePattern.matcher(initialMessageValue).replaceAll("${messageKey}");
		String overriddenDefault = this.propertyResolver.getProperty(key);
		if (null != overriddenDefault) {
			return overriddenDefault;
		}
		return this.resolveDefaultMessage(initialMessageValue, annotatedElement, annotation);
	}

	private String resolveDefaultMessage(String initialMessageValue, AnnotatedElement annotatedElement,
			Annotation annotation) {
		String key = this.replacePattern.matcher(initialMessageValue).replaceAll("${messageKey}");
		try {
			return this.defaultMessagesBundle.getString(key);
		} catch (MissingResourceException mre) {
			String annotationName = ANNOTATION_PREFIX + annotation.annotationType().getSimpleName();
			LOG.warn(() -> "Default message not defined for " + initialMessageValue
					+ ". It is likely validation logic is not supported for " + annotationName
					+ ". Please consider opening a feature request for " + annotationName
					+ " validation support or remove " + annotationName + " from " + annotatedElement + ".");
			return initialMessageValue;
		} catch (ClassCastException cce) {
			throw new FrameworkRuntimeException("Unable to resolve default mesage for " + initialMessageValue, cce);
		}
	}
}
