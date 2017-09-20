/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
 
/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Execution {

	/**
	 * Only the first execution config would have access to supplied payload.
	 *  
	 * @author Soham Chakravarti
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@Repeatable(Configs.class)
	@Execution
	public @interface Config {
		
		String url();
		
		String col() default "";
	}

}
