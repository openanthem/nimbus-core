/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.event;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.Event;

/**
 * @author Soham Chakravarti
 *
 */
public final class ConfigEvent {

	@Retention(RUNTIME)
	@Target(ANNOTATION_TYPE)
	@Event
	@Inherited
	public @interface OnParamCreate {

	}
}
