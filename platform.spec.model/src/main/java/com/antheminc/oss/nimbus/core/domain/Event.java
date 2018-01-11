/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker annotation for framework generated events
 * 
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Inherited
public @interface Event {

}
