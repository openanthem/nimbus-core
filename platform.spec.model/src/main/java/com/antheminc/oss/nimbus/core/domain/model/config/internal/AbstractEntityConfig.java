/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.config.internal;

import java.util.concurrent.atomic.AtomicInteger;

import com.antheminc.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.core.util.JustLogit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
abstract public class AbstractEntityConfig<T> implements EntityConfig<T> {

	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());

	private AnnotationConfig uiStyles;

	@JsonIgnore private RulesConfig rulesConfig; 
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	private final String configId = String.valueOf(counter.incrementAndGet());

	private EventHandlerConfig eventHandlerConfig;
}
