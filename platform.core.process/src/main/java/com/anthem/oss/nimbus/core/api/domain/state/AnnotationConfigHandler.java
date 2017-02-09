/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import com.anthem.nimbus.platform.spec.model.dsl.config.AnnotationConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public class AnnotationConfigHandler {
	
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
				
				AnnotationAttributes aa = AnnotationUtils.getAnnotationAttributes(aElem, a, false, true);
				HashMap<String, Object> map = new HashMap<>();
				for(String k : aa.keySet()) {
					map.put(k, aa.get(k));
				}
				ac.setAttributes(map);
				
				aConfigs.add(ac);
			}
		}
		
		return CollectionUtils.isEmpty(aConfigs) ? null : aConfigs;
	}
}
