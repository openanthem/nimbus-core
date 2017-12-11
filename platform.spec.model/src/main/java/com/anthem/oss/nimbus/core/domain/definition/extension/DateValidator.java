package com.anthem.oss.nimbus.core.domain.definition.extension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 
public class DateValidator implements ConstraintValidator<DateRange, LocalDate> {
    
	private static final String DATE_FORMAT = new String("MM/dd/yyyy");
    
    private DateRange constraintAnnotation;
    
    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }
    
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        try {
            final LocalDate min = LocalDate.parse(constraintAnnotation.min(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            final LocalDate max = LocalDate.parse(constraintAnnotation.max(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            return value == null ||
                    (value.isAfter(min) && value.isBefore(max));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
