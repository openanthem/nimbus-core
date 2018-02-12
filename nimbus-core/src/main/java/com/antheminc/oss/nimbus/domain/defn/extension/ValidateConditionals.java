package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.RepeatContainer;

/**
 * 
 * @author Tony Lopez
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(ValuesConditional.class)
public @interface ValidateConditionals {

	ValidateConditional[] value();
}
