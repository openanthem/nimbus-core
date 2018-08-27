package com.antheminc.oss.nimbus.support;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Rakesh Patel
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface InjectSelf {

}
