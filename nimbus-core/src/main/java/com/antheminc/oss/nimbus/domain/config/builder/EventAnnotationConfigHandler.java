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
/**
 * 
 */
package com.antheminc.oss.nimbus.domain.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.RepeatContainer;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class EventAnnotationConfigHandler implements AnnotationConfigHandler {
	
	@Override
	public AnnotationConfig handleSingle(AnnotatedElement aElem, Class<? extends Annotation> metaAnnotationType) {
		throw new UnsupportedOperationException("Operation not supported by EventAnnotationConfigHandler - only supports repeatable meta event annotations");
	}
	
	@Override
	public List<AnnotationConfig> handle(AnnotatedElement aElem, Class<? extends Annotation> metaAnnotationType) {
		throw new UnsupportedOperationException("Operation not supported by EventAnnotationConfigHandler - only supports repeatable meta event annotations");
	}
	
	@Override
	public List<Annotation> handleRepeatable(AnnotatedElement aElem, Class<? extends Annotation> repeatableMetaAnnotationType) {
		final List<Annotation> annotations = new ArrayList<>();
		
		final Annotation arr[] = aElem.getAnnotations();

		for(final Annotation currDeclaredAnnotation : arr) {
			final Set<String> metaTypesOnCurrDeclaredAnnotation = AnnotatedElementUtils.getMetaAnnotationTypes(aElem, currDeclaredAnnotation.annotationType());
			
			// handle repeatable container
			if(metaTypesOnCurrDeclaredAnnotation!=null && metaTypesOnCurrDeclaredAnnotation.contains(RepeatContainer.class.getName())) {
				
				// get repeat container meta annotation and use declared repeatable annotation
				RepeatContainer repeatContainerMetaAnnotation = AnnotationUtils.getAnnotation(currDeclaredAnnotation, RepeatContainer.class);
				Class<? extends Annotation> repeatableAnnotationDeclared = repeatContainerMetaAnnotation.value();
				
				// check that the declared annotation has passed in meta annotation type
				
				boolean hasRepeatable = AnnotatedElementUtils.hasAnnotation(repeatableAnnotationDeclared, Event.class);
				if(hasRepeatable) {
					Object value = AnnotationUtils.getValue(currDeclaredAnnotation);
					if(value==null || !value.getClass().isArray())
						throw new InvalidConfigException("Repeatable container annotation is expected to follow convention: '{RepeableAnnotationType}[] value();' but not found in: "+currDeclaredAnnotation);
					
					Annotation[] annArr = (Annotation[])value;
					Stream.of(annArr).forEach(ann -> {
						addIfcontainsAnnotationAsEventType(ann, repeatableMetaAnnotationType, annotations);
					});
				}
			}
			
			// handle non-repeating meta annotation
			if(metaTypesOnCurrDeclaredAnnotation != null && metaTypesOnCurrDeclaredAnnotation.contains(Event.class.getName())) {
				addIfcontainsAnnotationAsEventType(currDeclaredAnnotation, repeatableMetaAnnotationType, annotations);
			}
		}
		
		return annotations;
	}
	
	private void addIfcontainsAnnotationAsEventType(Annotation ann, Class<? extends Annotation> repeatableMetaAnnotationType, final List<Annotation> annotations) {
		Object eventTypes = AnnotationUtils.getValue(ann, "eventType");
			
		if(eventTypes != null) {
			if(!(eventTypes instanceof EventType[]))
				throw new InvalidConfigException("eventType attribute must contain elements of type "+EventType.class.getName()+" for annotation "+ann);
				
			if(containsRepeatableMetaAnnotation((EventType[])eventTypes, repeatableMetaAnnotationType))
				annotations.add(ann);
		}
		else {
			findEventMetaAnnotationRecursively(ann, ann.annotationType().getDeclaredAnnotations(), repeatableMetaAnnotationType, annotations);
		}
	}

	/**
	 * do a meta (recursive) search on the annotated element for the {@link Event } type
	 *
	 * @param annotatedElement the annotated element
	 * @param foundAnnotations the already found annotations	
	 */
	private void findEventMetaAnnotationRecursively(final Annotation annotation, final Annotation[] declaredAnnotations, Class<? extends Annotation> repeatableMetaAnnotationType, List<Annotation> annotations) {
		for(Annotation a : declaredAnnotations) {
			boolean b = a.annotationType() == Event.class || AnnotatedElementUtils.hasAnnotation(a.annotationType(), Event.class);
			if(b) {
				Object eventTypes = AnnotationUtils.getValue(a, "eventType");
				if(eventTypes != null) {
					if(!(eventTypes instanceof EventType[]))
						throw new InvalidConfigException("eventType attribute must contain elements of type "+EventType.class.getName()+" for annotation "+a);
					
					if(containsRepeatableMetaAnnotation((EventType[])eventTypes, repeatableMetaAnnotationType))
						annotations.add(annotation);
					break;
				}
				else {
					findEventMetaAnnotationRecursively(annotation, a.annotationType().getDeclaredAnnotations(), repeatableMetaAnnotationType, annotations);
				}
			}
		}
	}

	private boolean containsRepeatableMetaAnnotation(EventType[] eventTypes, Class<? extends Annotation> repeatableMetaAnnotationType) {
		return Optional.ofNullable(eventTypes)
				.map(Stream::of)
				.orElse(Stream.empty())
				.anyMatch(e -> e.getAnnotationType() == repeatableMetaAnnotationType);
		
	}
	
}

