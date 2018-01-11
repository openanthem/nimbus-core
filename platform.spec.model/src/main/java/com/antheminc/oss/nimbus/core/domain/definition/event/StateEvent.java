/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.core.domain.Event;

/**
 * @author Soham Chakravarti
 *
 */
public final class StateEvent {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Event
	@Inherited
	public @interface OnStateLoad {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Event
	@Inherited
	public @interface OnStateChange {

	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Event
	@Inherited
	public @interface OnTxnExecute {

	}
}
