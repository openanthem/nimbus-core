/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.Implementation;
import com.anthem.oss.nimbus.core.domain.definition.OnStateChange;
import com.anthem.oss.nimbus.core.domain.model.state.StateNotificationHandler;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateChange
public @interface ActivateConditional {

	/**
	 * path of param to activate when condition is satisfied 
	 */
	String targetPath();
	
	/**
	 * expression to evaluate against declared param's state, <br> 
	 * if true would lookup implementation for {@linkplain StateNotificationHandler} annotation with {@linkplain Implementation}  
	 */
	String when();
	
	/**
	 * condition on which param would be inactivated. <br>
	 * by default, if no value is provided, then the negation of {@linkplain ActivateConditional#when()} would be used 
	 */
	String inactivateWhen() default "";
}
