package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.RepeatContainer;

/**
 * 
 * @see com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional
 * @author Tony Lopez (AF42192)
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(ValuesConditional.class)
public @interface ValuesConditionals {

	/**
	 * 
	 */
	ValuesConditional[] value();
}
