/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
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
@OnStateLoad @OnStateChange
public @interface SetConditional {
	
	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when();
	
	Then[] then();
	
	public @interface Then {
		
		String targetParam();
		
		/**
		 *  SpEL based expression to be executed relative to param on which this annotation is declared.
		 */
		String stateExr();
	}
}
