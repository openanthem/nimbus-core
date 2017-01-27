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
public interface SearchNature {

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.FIELD)
	public @interface StartsWith {
	    String wildCard() default "*";
	}
	
}
