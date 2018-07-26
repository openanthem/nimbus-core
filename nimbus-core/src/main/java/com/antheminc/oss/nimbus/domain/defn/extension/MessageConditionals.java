package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.RepeatContainer;



/**
 * <p>Repeatable Container class for <tt>MessageConditional</tt>.
 * 
 * @see com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional
 * @author Akancha Kashyap
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(MessageConditional.class)
public @interface MessageConditionals {

	/**
	 * <p>The array of <tt>&#64;MessageConditional</tt> annotations.</p>
	 */
	MessageConditional[] value();
}
