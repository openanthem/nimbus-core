/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Repeatable(OnStateChanges.class)
@Inherited
public @interface OnStateChange {

}
