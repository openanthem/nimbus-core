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
package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.RepeatContainer;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

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
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.AbstractValuesConditionalStateEventHandler
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
	
	int order() default Event.DEFAULT_ORDER_NUMBER;
}
