/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */
public interface EventHandlerConfig {

	public Set<AnnotationConfig> getOnStateLoadAnnotationConfigs();
	public Optional<OnStateLoadHandler<Annotation>> findOnStateLoadHandler(AnnotationConfig ac);
	public OnStateLoadHandler<Annotation> getOnStateLoadHandler(AnnotationConfig ac) throws InvalidConfigException;
	
	
	public Set<AnnotationConfig> getOnStateChangeAnnotationConfigs();
	public Optional<OnStateChangeHandler<Annotation>> findOnStateChangeHandler(AnnotationConfig ac);
	public OnStateChangeHandler<Annotation> getOnStateChangeHandler(AnnotationConfig ac) throws InvalidConfigException;
	
 }
