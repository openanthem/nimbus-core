/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(EnableConditionals.class)
@OnStateChange @OnStateLoad
public @interface EnableConditional {

	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when();
	
	/**
	 * Path of param to enable when condition is satisfied relative to param on which this annotation is declared
	 */
	String[] targetPath();
	
	/**
	 * SpEL based condition on which param would be disabled. <br>
	 * If value is not overridden, then the negation of {@linkplain EnableConditional#when()} would be used 
	 */
	String disableWhen() default "";

}
