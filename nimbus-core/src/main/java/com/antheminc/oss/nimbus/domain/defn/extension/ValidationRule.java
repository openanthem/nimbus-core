/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.defn.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;


/**
 * <p>A set of client-side validation rules to perform. When a given
 * {@link RuleSet} evaluation evaluates to {@code false}, the UI will render the
 * decorated form element as {@code invalid} and display the associated error
 * message.</p> <p>ValidationRule should decorate a field also decorated with
 * {@link Form}. <p><b>Usage</b><br></p>
 * 
 * <pre>
 * &#64;ValidationRule(ruleset = {
 *     &#64;RuleSet(rule = "findStateByPath('/endDate') > findStateByPath('/startDate')", message = "End date must be greater than start date")
 * })
 * &#64;Form
 * private SampleForm sampleForm;
 * 
 * &#64;Model
 * &#64;Data
 * public static class SampleForm {
 * 
 *     &#64;Calendar
 *     private LocalDate startDate;
 * 
 *     &#64;Calendar
 *     private LocalDate endDate;
 * }
 * </pre>
 * 
 * </ul>
 * 
 * @author Sandeep Mantha	
 */	 
@Documented
@Constraint(validatedBy = {  })
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationRule {
	/**
	 * <p>One or more client-side rule validations to evaluate against the
	 * decorated {@link Form}. Rulesets that fail will have their failure
	 * messages displayed atop the rendered form element.
	 */
	public RuleSet[] ruleset();

	public @interface RuleSet1 {
		
		String message() default "";
		String rule() default "";
		
	}
}