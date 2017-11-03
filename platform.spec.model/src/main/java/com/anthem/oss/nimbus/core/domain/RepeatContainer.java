package com.anthem.oss.nimbus.core.domain;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation validator implementation should verify that the annotated element does have "value()" declared returning an 
 * array of repeatable annotations
 *  
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface RepeatContainer {

}
