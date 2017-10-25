package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;
 
/**
 * Used to provide the exclusion list. 
 * E.g. 
 * 		if a param needs to be read only for a user role = "intake":
 * 		@AcccessConditional(value
 * @author Rakesh Patel
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateLoad
public @interface AccessConditional {
	
	@AliasFor("value")
	R2P[] roleToPermissions() default {};
	
	@AliasFor("roleToPermissions")
	R2P[] value() default {};
	
	@Retention(RUNTIME)
	@Target(FIELD)
	@interface R2P {
		
		/**
		 * Role of the current logged in user 
		 */
		String r() default "";
		
		/**
		 * Permissions for a given role to exclude
		 */
		Permission[] p() default {};
	}
	
	
	public enum Permission {
		WRITE,
		READ;
	}
}
