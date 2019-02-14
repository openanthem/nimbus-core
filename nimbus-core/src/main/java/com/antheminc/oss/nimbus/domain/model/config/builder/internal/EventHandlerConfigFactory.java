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
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.defn.event.ConfigEvent.OnParamCreate;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoadNew;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.event.ConfigEventHandlers.OnParamCreateHandler;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultEventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadNewHandler;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class EventHandlerConfigFactory {

	private final BeanResolverStrategy beanResolver;
	
	private AnnotationConfigHandler annotationConfigHandler;
	
	public EventHandlerConfigFactory(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.annotationConfigHandler = beanResolver.find(AnnotationConfigHandler.class, "eventAnnotationConfigBuilder");
	}
	
	@SuppressWarnings("unchecked")
	public EventHandlerConfig build(AnnotatedElement aElem) {
		final DefaultEventHandlerConfig eventConfig = new DefaultEventHandlerConfig();
		
		// onParamCreate
		buildInternal(aElem, OnParamCreate.class, OnParamCreateHandler.class, (a,h)->eventConfig.add(a, h));
		
		// onStateLoad
		buildInternal(aElem, OnStateLoad.class, OnStateLoadHandler.class, (a,h)->eventConfig.add(a, h));

		// onStateLoadNew
		buildInternal(aElem, OnStateLoadNew.class, OnStateLoadNewHandler.class, (a,h)->eventConfig.add(a, h));
		
		// onStateChange
		buildInternal(aElem, OnStateChange.class, OnStateChangeHandler.class, (a,h)->eventConfig.add(a, h));
		
		return eventConfig.isEmpty() ? null : eventConfig;
	}

	
	protected <T> void buildInternal(AnnotatedElement aElem, Class<? extends Annotation> configuredAnnotationType, Class<T> handlerType, BiConsumer<Annotation, T> addHandlerCb) {
		List<Annotation> annotations = getAnnotationConfigHandler().handleRepeatable(aElem, configuredAnnotationType);
		if(!CollectionUtils.isEmpty(annotations)) { 
			
			annotations.stream()
				.forEach(a->{
					T handler = getBeanResolver().get(handlerType, a.annotationType());
					addHandlerCb.accept(a, handler);
				});
		}
	}
	
}
