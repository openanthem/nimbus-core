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
import com.anthem.oss.nimbus.core.domain.definition.event.CommandEvents.OnRootExecutes;
import com.anthem.oss.nimbus.core.domain.definition.event.CommandEvents.OnSelfExecutes;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandEvent {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnRootExecutes.class)
	@Event
	@Inherited
	public @interface OnRootExecute {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnSelfExecutes.class)
	@Event
	@Inherited
	public @interface OnSelfExecute {

	}
}
