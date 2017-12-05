package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.RepeatContainer;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;

/**
 * This annotation is used to provide control and management over the <tt>Values</tt> 
 * property of a param's state through the use of conditional SpEL statements. <br>
 * 
 * <p>
 * 1. Update the values of <tt>statusReason</tt> from <tt>SR_ALL.class</tt> to 
 * <tt>SR_A.class</tt> when the value of <tt>status</tt> is 'A'.
 * </p>
 * 
 * <pre>
 * &#64;ValuesConditional(target = "../statusReason", condition = { 
 *     &#64;Condition(when = "state == 'A'", then = &#64;Values(SR_A.class))
 * })
 * private String status;
 * 
 * &#64;Values(SR_ALL.class)
 * private String statusReason;
 * </pre>
 * 
 * <p>
 * 2. Set multiple conditions and even override conditions by setting the
 * <tt>exclusive</tt> property. In this case, it is possible to give priority to
 * the last conditional checking for state 'A'.
 * </p>
 * <pre>
 * &#64;ValuesConditional(target = "../statusReason", condition = { 
 *     &#64;Condition(when = "state=='A'", then = &#64;Values(SR_A.class))
 *     &#64;Condition(when = "state=='B'", then = &#64;Values(SR_B.class))
 *     &#64;Condition(when = "state=='A'", then = &#64;Values(SR_C.class))
 * })
 * private String status;
 * 
 * &#64;Values(SR_ALL.class)
 * private String statusReason;
 * </pre>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.anthem.oss.nimbus.core.domain.model.state.extension.AbstractValuesConditionalStateEventHandler
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ValuesConditionals.class)
@OnStateChange @OnStateLoad
public @interface ValuesConditional {

	/**
	 * The target path relative to the this annotated field to update.
	 */
	String target();
	
	/**
	 * An array of conditional metadata that is responsible for determining whether
	 * or not the <tt>target</tt> field will be updated.
	 * 
	 * <p>Conditions are executed from top to bottom, in the order in which they
	 * are defined. If it is necessary to define logic to override prior conditions, 
	 * consider setting the <tt>exclusive</tt> value to false.
	 */
	Condition[] condition();
	
	/**
	 * Configures whether or not the first truthy condition should be exclusive.
	 * 
	 * <p>If true, then only the first truthy condition will be executed. If false, 
	 * then all truthy conditions will be executed. the default value is true.
	 */
	boolean exclusive() default true;
	
	/**
	 * <p>Whether or not to reset the state of the target field when the associated
	 * <tt>&#64;Values</tt> property is updated.</p>
	 * 
	 * <p>If a condition is truthy and <tt>resetOnChange</tt> is <b>false</b>, then state
	 * will be attempt to be preserved. State is preserved in this scenario only if the value 
	 * of state is found within the newly updated <tt>&#64;Values</tt> property. If it is 
	 * not found, the state will be reset.</p>
	 * 
	 * <p>If <tt>resetOnChange</tt> is <b>true</b>, state will always be reset on change.</p>
	 * 
	 * <p>The default value is <b>true</b>.
	 */
	boolean resetOnChange() default true;
	
	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Conditions.class)
	public @interface Condition {
		
		/**
		 * SpEL based condition to be evaluated relative to param's state on which 
		 * this annotation is declared.
		 */
		String when();
		
		/**
		 * <tt>Values</tt> configuration to be applied to the param identified by
		 * the <tt>target</tt> path when this condition's <tt>when</tt> clause is
		 * found to be true.
		 */
		Values then();
	}
	
	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@RepeatContainer(Condition.class)
	public @interface Conditions {
		
		/**
		 * A single or set of conditions to evaluate against.
		 */
		Condition[] value();
	}
}
