/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.ExecutionRuntimeEvent.OnRuntimeStart;
import com.anthem.oss.nimbus.core.domain.definition.event.ExecutionRuntimeEvent.OnRuntimeStop;

/**
 * @author Soham Chakravarti
 *
 */
public class ExecutionRuntimeEvents {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnRuntimeStarts {

		OnRuntimeStart[] value();
	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnRuntimeStops {

		OnRuntimeStop[] value();
	}
}
