/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.Event;
import com.anthem.oss.nimbus.core.domain.definition.event.ExecutionRuntimeEvents.OnRuntimeStarts;
import com.anthem.oss.nimbus.core.domain.definition.event.ExecutionRuntimeEvents.OnRuntimeStops;

/**
 * @author Soham Chakravarti
 *
 */
public final class ExecutionRuntimeEvent {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnRuntimeStarts.class)
	@Event
	@Inherited
	public @interface OnRuntimeStart {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnRuntimeStops.class)
	@Event
	@Inherited
	public @interface OnRuntimeStop {

	}
}
