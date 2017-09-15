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
		List<AnnotationConfig> aConfigs = handle(aElem, metaAnnotationType);
		if(CollectionUtils.isEmpty(aConfigs)) return null;
		
		if(aConfigs.size()!=1) throw new InvalidConfigException("Found more than one element of config :" +aConfigs+ " Expecting only one config element");
		
		return aConfigs.get(0);
	}

	public static List<AnnotationConfig> handle(AnnotatedElement aElem, Class<? extends Annotation> metaAnnotationType) {
		boolean hasIt = AnnotatedElementUtils.hasMetaAnnotationTypes(aElem, metaAnnotationType);
		if(!hasIt) return null;

		List<AnnotationConfig> aConfigs = new ArrayList<>();
		
		Annotation arr[] = aElem.getAnnotations();
		for(Annotation a : arr) {
			Set<String> metaTypes = AnnotatedElementUtils.getMetaAnnotationTypes(aElem, a.annotationType());
			
			if(metaTypes!=null && metaTypes.contains(metaAnnotationType.getName())) {
				AnnotationConfig ac = new AnnotationConfig();
				ac.setName(ClassUtils.getShortName(a.annotationType()));
				ac.setAttributes(getAttributesHandlerForType(metaAnnotationType).generateFrom(aElem, a));
				aConfigs.add(ac);
			}
		}
		
		return CollectionUtils.isEmpty(aConfigs) ? null : aConfigs;
	}

	/**
	 * 
	 * @param metaAnnotationType
	 * @return
	 */
	private static AnnotationAttributeHandler getAttributesHandlerForType(Class<? extends Annotation> metaAnnotationType) {
		return Optional.ofNullable(attributeHandlers.get(metaAnnotationType)).orElse(defaultAttributeHandler);
	}
}
