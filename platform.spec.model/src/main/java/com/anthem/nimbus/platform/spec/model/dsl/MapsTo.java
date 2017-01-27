/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
public class MapsTo {

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Model {
		
		Class<?> value();

		/**
		 * throw exception if unmapped property is found. Defaults to silent, i.e., no exception thrown 
		 */
		boolean silent() default true;
	}
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Path {
		
		String value() default "";
		
		boolean linked() default true;
	}
	
	
	@Path
	public static final String DEFAULT_PATH = "";
	
}
