/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Implementation {

	/**
	 * Handler implementation for events annotated with this annotation class 
	 */
	Class<? extends Annotation> value();
}
