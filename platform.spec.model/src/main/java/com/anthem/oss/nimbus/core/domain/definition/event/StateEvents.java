/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnTxnExecute;

/**
 * @author Soham Chakravarti
 *
 */
public final class StateEvents {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnStateLoads {

		OnStateLoad[] value();
	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnStateChanges {

		OnStateChange[] value();
	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnTxnExecutes {

		OnTxnExecute[] value();
	}
	
}
