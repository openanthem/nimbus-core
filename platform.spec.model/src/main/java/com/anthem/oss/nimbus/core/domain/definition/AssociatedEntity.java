package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rakesh Patel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.FIELD})
@Inherited
public @interface AssociatedEntity {
	
	public String value() default "";
	
	public Class<?> clazz() default Class.class;
	
}