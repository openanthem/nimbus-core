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

	public Set<Annotation> getOnStateLoadAnnotations();
	public Optional<OnStateLoadHandler<Annotation>> findOnStateLoadHandler(Annotation a);
	public OnStateLoadHandler<Annotation> getOnStateLoadHandler(Annotation a) throws InvalidConfigException;
	
	
	public Set<Annotation> getOnStateChangeAnnotations();
	public Optional<OnStateChangeHandler<Annotation>> findOnStateChangeHandler(Annotation a);
	public OnStateChangeHandler<Annotation> getOnStateChangeHandler(Annotation a) throws InvalidConfigException;
	
 }
