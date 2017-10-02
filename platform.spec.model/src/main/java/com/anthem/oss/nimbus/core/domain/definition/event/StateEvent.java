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
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvents.OnStateChanges;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvents.OnStateLoads;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvents.OnTxnExecutes;

/**
 * @author Soham Chakravarti
 *
 */
public final class StateEvent {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnStateLoads.class)
	@Event
	@Inherited
	public @interface OnStateLoad {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnStateChanges.class)
	@Event
	@Inherited
	public @interface OnStateChange {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Repeatable(OnTxnExecutes.class)
	@Event
	@Inherited
	public @interface OnTxnExecute {

	}
}
