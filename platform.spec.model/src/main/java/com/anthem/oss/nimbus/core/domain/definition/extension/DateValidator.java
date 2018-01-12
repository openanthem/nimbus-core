/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
 
/**
 * @author Sandeep Mantha
 *
 */
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
