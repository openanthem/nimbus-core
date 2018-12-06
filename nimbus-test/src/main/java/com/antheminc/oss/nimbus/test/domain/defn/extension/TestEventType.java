package com.antheminc.oss.nimbus.test.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

/**
 * @author Rakesh Patel
 *
 */

public class TestEventType {
	
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(EventNoOverrides.class)
	@Event(eventType=OnStateLoad.class)
	public @interface EventNoOverride {

		String value() default "";
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	@Event
	public @interface EventAllowOverride {
		
		String value() default "";
		
		Class<? extends Annotation>[] eventType() default {OnStateChange.class};
	}
	
	@Retention(RUNTIME)
	@Target(FIELD)
	@OnStateChange
	public @interface OnStateChangeNoOverride {
		
		String value() default "";
	}
	
	
	/**
	 * <p> Hides the meta annotation {@code OnStateChange} through <code>eventType</code> attribute </p>
	 * <p> Event declared using eventType() attribute overrides the event declared through meta annotation. possible but should not be used </p>
	 * 
	 */
	@Retention(RUNTIME)
	@Target(FIELD)
	@OnStateChange
	public @interface ConflictingEventOverride {
		
		String value() default "";
		
		Class<? extends Annotation>[] eventType() default {OnStateLoad.class};
	}
	
	/**
	 * <p> Hides the meta annotation {@code OnStateChange} through <code>eventType</code> attribute </p>
	 * <p> Event declared using eventType() attribute overrides the event declared through meta annotation. redundant configuration </p>
	 * 
	 */
	@Retention(RUNTIME)
	@Target(FIELD)
	@OnStateChange
	public @interface RedundantEventOverride {
		
		String value() default "";
		
		Class<? extends Annotation>[] eventType() default {OnStateChange.class};
	}
	
	/**
	 * <p> Event declared using empty <code>eventType</code> attribute and empty meta {@link Event}</p>
	 * <p> Declaring this annotation must provide <code>eventType</code>, else throws {@link InvalidConfigException} 
	 * 
	 */
	@Retention(RUNTIME)
	@Target(FIELD)
	@Event
	public @interface EventMandatoryOverride {
		
		String value() default "";
		
		Class<? extends Annotation>[] eventType() default {};
	}
}



