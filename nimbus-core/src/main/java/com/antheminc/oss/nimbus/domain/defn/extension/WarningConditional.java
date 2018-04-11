package com.antheminc.oss.nimbus.domain.defn.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;


@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@OnStateLoad
@OnStateChange
public @interface WarningConditional {
	
	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when() default "";
	/**
	 *  The Warning Message displayed if the Spel condition evaluates to false.
	 */
	String message() default "";
	/**
	 *  The Type of Warning - dictates UI properties of the warning
	 */
	WarningType warningType() default WarningType.Mild;
	public enum WarningType{
		Mild,
		Severe;
	}

}