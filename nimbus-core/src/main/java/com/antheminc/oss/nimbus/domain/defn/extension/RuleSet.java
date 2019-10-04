package com.antheminc.oss.nimbus.domain.defn.extension;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;	






/**

 * <p>A validation rule to perform. When the provided {@link #rule() evaluates
 * to {@code false}, the UI will render the decorated form element as
 * {@code invalid} and display the provided {@link #message()}.</p>
 * <p><b>Usage</b><br></p>
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
@Retention(RetentionPolicy.RUNTIME)
public @interface RuleSet {
	

	/**
	 * <p>The message to be displayed when the {@link #rule()} expression
	 * evaluates to {@code false}.
	 */

	String message() default "";		
	

	/**
	 * <p>A typescript syntaxed evaluation to perform. <p><b>Syntax</b><br>
	 * <p>Any valid typescript syntax is supported. Some additional framework
	 * related methods are also supported. See the supported methods below for
	 * more information.</p> <p><b>Supported Methods</b><br> <ul>
	 * <li><b>findStateByPath(path: string)</b> - Find the state of a param,
	 * where {@code path} is a param path relative to the decorated form
	 * param</li> </ul>
	 */
	String rule() default "";

} 	

