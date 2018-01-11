package com.antheminc.oss.nimbus.core.domain.definition.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Sandeep Mantha
 *
 */

@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {
    String message() default "Date entered must be within range.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String min() default "01/01/2017";
    String max() default "12/31/2999";
}
