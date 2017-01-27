/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Soham Chakravarti
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=PrefixValidator.class)
public @interface Prefix {

	String message() default "{com.anthem.nimbus.platform.spec.model.validation.Prefix.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String value();

	String pattern() default "";

	
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {

		Prefix[] value();
	}
	
}
