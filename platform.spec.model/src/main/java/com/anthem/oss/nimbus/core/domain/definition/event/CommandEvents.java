/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.CommandEvent.OnRootExecute;
import com.anthem.oss.nimbus.core.domain.definition.event.CommandEvent.OnSelfExecute;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandEvents {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnRootExecutes {

		OnRootExecute[] value();
	}
	
	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Inherited
	public @interface OnSelfExecutes {

		OnSelfExecute[] value();
	}
}
