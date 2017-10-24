/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ActivateConditionals.class)
@OnStateChange @OnStateLoad
public @interface ActivateConditional {

	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when();
	
	/**
	 * Path of param to activate when condition is satisfied relative to param on which this annotation is declared
	 */
	String[] targetPath();
	
	/**
	 * SpEL based condition on which param would be inactivated. <br>
	 * If value is not overridden, then the negation of {@linkplain ActivateConditional#when()} would be used 
	 */
	String inactivateWhen() default "";
}
