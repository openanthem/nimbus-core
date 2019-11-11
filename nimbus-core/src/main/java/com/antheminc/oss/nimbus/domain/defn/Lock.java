package com.antheminc.oss.nimbus.domain.defn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnRootParamLock;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnRootParamUnlock;

/**
 * @author Sandeep Mantha
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
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
