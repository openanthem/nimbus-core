package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.RepeatContainer;

/**
 * <p>Repeatable Container class for <tt>ValidateConditional</tt>.
 * 
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional
 * @author Tony Lopez
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(ValuesConditional.class)
public @interface ValidateConditionals {

	/**
	 * <p>The array of <tt>&#64;ValidateConditional</tt> annotations.</p>
	 */
	ValidateConditional[] value();
}
