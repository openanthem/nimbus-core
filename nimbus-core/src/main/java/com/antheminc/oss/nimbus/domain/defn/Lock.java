package com.antheminc.oss.nimbus.domain.defn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sandeep Mantha
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Model
public @interface Lock {
	
	boolean root() default false;
	
	String message() default "";
	
	String alias() default "lock";
	
	Strategy strategy() default Strategy.message;
	
	enum Strategy {
		message,
		view
		
	}
}
