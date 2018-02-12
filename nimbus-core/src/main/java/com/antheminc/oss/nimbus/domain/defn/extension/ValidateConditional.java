package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

/**
 * 
 * @author Tony Lopez
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ValidateConditionals.class)
@OnStateChange @OnStateLoad
public @interface ValidateConditional {

	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when();
	
	/**
	 * 
	 */
	Class<? extends ValidationGroup> targetGroup();
	
	/**
	 * 
	 */
	ValidationScope scope() default ValidationScope.SIBLING;
	
	/**
	 * 
	 */
	public interface ValidationGroup {};
	
	public interface GROUP_0 extends ValidationGroup {};
	public interface GROUP_1 extends ValidationGroup {};
	public interface GROUP_2 extends ValidationGroup {};
	public interface GROUP_3 extends ValidationGroup {};
	public interface GROUP_4 extends ValidationGroup {};
	public interface GROUP_5 extends ValidationGroup {};
	public interface GROUP_6 extends ValidationGroup {};
	public interface GROUP_7 extends ValidationGroup {};
	public interface GROUP_8 extends ValidationGroup {};
	public interface GROUP_9 extends ValidationGroup {};
	
	public enum ValidationScope {
		SIBLING,
		SIBLING_NESTED;
	}
}
