/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Constraint;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import com.anthem.oss.nimbus.core.domain.config.builder.attributes.AnnotationAttributeHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.attributes.ConstraintAnnotationAttributeHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.attributes.DefaultAnnotationAttributeHandler;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;

/**
 * @author Soham Chakravarti
 *
 */
public class AnnotationConfigHandler {
	
	/**
	 * Default Handler for generating attribute values
	 */
	private static AnnotationAttributeHandler defaultAttributeHandler = new DefaultAnnotationAttributeHandler();
	
	// TODO Move to spring context
	private static Map<Class<?>, AnnotationAttributeHandler> attributeHandlers = new HashMap<>();
	static {
		attributeHandlers.put(Constraint.class, new ConstraintAnnotationAttributeHandler());
	}
	
	public static AnnotationConfig handleSingle(AnnotatedElement aElem, Class<? extends Annotation> metaAnnotationType) {
		final List<AnnotationConfig> aConfigs = handle(aElem, metaAnnotationType);
		if(CollectionUtils.isEmpty(aConfigs)) {
			return null;
		}
		
		if(aConfigs.size() != 1) {
			throw new InvalidConfigException(String.format("Found more than one element of config: %s. Expecting only one config element", aConfigs));
		}
		
		return aConfigs.get(0);
	}

	public static List<AnnotationConfig> handle(AnnotatedElement aElem, Class<? extends Annotation> metaAnnotationType) {
		final boolean hasIt = AnnotatedElementUtils.hasMetaAnnotationTypes(aElem, metaAnnotationType);
		if (!hasIt) {
			return null;
		}

		final List<AnnotationConfig> aConfigs = new ArrayList<>();
		
		final Annotation arr[] = aElem.getAnnotations();
		for(final Annotation a : arr) {
			final Set<String> metaTypes = AnnotatedElementUtils.getMetaAnnotationTypes(aElem, a.annotationType());
			
			if (metaTypes != null && metaTypes.contains(metaAnnotationType.getName())) {
				final AnnotationConfig ac = new AnnotationConfig();
				ac.setName(ClassUtils.getShortName(a.annotationType()));
				ac.setAttributes(getAttributesHandlerForType(metaAnnotationType).generateFrom(aElem, a));
				aConfigs.add(ac);
			}
		}
		
		// TODO null may be unreachable
		return CollectionUtils.isEmpty(aConfigs) ? null : aConfigs;
	}

	/**
	 * If an <tt>AnnotationAttributeHandler</tt> is registered for the provided type of
	 * <tt>metaAnnotationType</tt> it will be returned.
	 * 
	 * Otherwise this instance's <tt>defaultAttributeHandler</tt> will be returned.
	 * 
	 * @param metaAnnotationType
	 * @see com.anthem.oss.nimbus.core.domain.config.builder.attributes.DefaultAnnotationAttributeHandler
	 * @return
	 */
	private static AnnotationAttributeHandler getAttributesHandlerForType(Class<? extends Annotation> metaAnnotationType) {
		return Optional.ofNullable(attributeHandlers.get(metaAnnotationType)).orElse(defaultAttributeHandler);
	}
}
