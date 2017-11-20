/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.RepeatContainer;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@RepeatContainer(AccessConditional.class)
public @interface AccessConditionals {
	
	AccessConditional[] value();
}
