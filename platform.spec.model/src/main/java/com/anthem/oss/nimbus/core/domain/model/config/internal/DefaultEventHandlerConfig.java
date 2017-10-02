/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.MapUtils;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultEventHandlerConfig implements EventHandlerConfig {

	private Map<AnnotationConfig, OnStateLoadHandler<Annotation>> onStateLoadHandlers;
	private Map<AnnotationConfig, OnStateChangeHandler<Annotation>> onStateChangeHandlers;
	
	public boolean isEmpty() {
		return MapUtils.isEmpty(getOnStateLoadHandlers()) ||
				MapUtils.isEmpty(getOnStateChangeHandlers());
 	}
	
	
	@Override
	public Set<AnnotationConfig> getOnStateLoadAnnotationConfigs() {
		return Optional.ofNullable(getOnStateLoadHandlers()).map(Map::keySet).orElse(null);
	}
	
	public void add(AnnotationConfig ac, OnStateLoadHandler<Annotation> handler) {
		Optional.ofNullable(getOnStateLoadHandlers()).orElseGet(()->{
			setOnStateLoadHandlers(new HashMap<>());
			return getOnStateLoadHandlers();
		}).put(ac, handler);
	}
	
	@Override
	public Optional<OnStateLoadHandler<Annotation>> findOnStateLoadHandler(AnnotationConfig ac) {
		return Optional.ofNullable(getOnStateLoadHandlers()).map(handlers->handlers.get(ac));
	}
	
	@Override
	public OnStateLoadHandler<Annotation> getOnStateLoadHandler(AnnotationConfig ac) throws InvalidConfigException {
		return findOnStateLoadHandler(ac).orElseThrow(()->getEx(ac));
	}
	
	
	@Override
	public Set<AnnotationConfig> getOnStateChangeAnnotationConfigs() {
		return Optional.ofNullable(getOnStateChangeHandlers()).map(Map::keySet).orElse(null);
	}
	
	public void add(AnnotationConfig ac, OnStateChangeHandler<Annotation> handler) {
		Optional.ofNullable(getOnStateChangeHandlers()).orElseGet(()->{
			setOnStateChangeHandlers(new HashMap<>());
			return getOnStateChangeHandlers();
		}).put(ac, handler);
	}
	
	@Override
	public Optional<OnStateChangeHandler<Annotation>> findOnStateChangeHandler(AnnotationConfig ac) {
		return Optional.ofNullable(getOnStateChangeHandlers()).map(handlers->handlers.get(ac));
	}
	
	@Override
	public OnStateChangeHandler<Annotation> getOnStateChangeHandler(AnnotationConfig ac) throws InvalidConfigException {
		return findOnStateChangeHandler(ac).orElseThrow(()->getEx(ac));
	}
	
	
	private InvalidConfigException getEx(AnnotationConfig ac) {
		return new InvalidConfigException("Expected event handler for annotation: "+ac.getAnnotation()+ " not found");
	}
}
