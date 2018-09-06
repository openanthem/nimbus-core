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
 * This annotation is used to provide control and management over the
 * {@code Values} property of a param's state through the use of conditional
 * SpEL statements. <br>
 * 
 * <p> 1. Update the values of {@code statusReason} from {@code SR_ALL.class} to
 * {@code SR_A.class} when the value of {@code status} is 'A'.
 * 
 * <pre> &#64;ValuesConditional(target = "../statusReason", condition = {
 * &#64;Condition(when = "state == 'A'", then = &#64;Values(SR_A.class)) })
 * private String status;
 * 
 * &#64;Values(SR_ALL.class) private String statusReason; </pre>
 * 
 * <p> 2. Set multiple conditions and even override conditions by setting the
 * {@code exclusive} property. In this case, it is possible to give priority to
 * the last conditional checking for state 'A'.
 * 
 * <pre> &#64;ValuesConditional(target = "../statusReason", condition = {
 * &#64;Condition(when = "state=='A'", then = &#64;Values(SR_A.class))
 * &#64;Condition(when = "state=='B'", then = &#64;Values(SR_B.class))
 * &#64;Condition(when = "state=='A'", then = &#64;Values(SR_C.class)) })
 * private String status;
 * 
 * &#64;Values(SR_ALL.class) private String statusReason; </pre>
 * 
 * @author Tony Lopez
 * @since 1.0
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ValuesConditionalStateEventHandler
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ValuesConditionals.class)
@OnStateChange
@OnStateLoad
public @interface ValuesConditional {

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Conditions.class)
	public @interface Condition {

		/**
		 * <p>{@link Values} configuration to be applied to the param identified
		 * by the {@code target} path when this condition's {@link #when()}
		 * clause is found to be true.
		 */
		Values then();

		/**
		 * <p>SpEL based condition to be evaluated relative to param's state on
		 * which this annotation is declared.
		 */
		String when();
	}

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@RepeatContainer(Condition.class)
	public @interface Conditions {

		/**
		 * <p>A single or set of conditions to evaluate against.
		 */
		Condition[] value();
	}

	/**
	 * <p>An array of conditional metadata that is responsible for determining
	 * whether or not the {@code target} field will be updated.
	 * 
	 * <p>Conditions are executed from top to bottom, in the order in which they
	 * are defined. If it is necessary to define logic to override prior
	 * conditions, consider setting the {@link #exclusive()} value to false.
	 */
	Condition[] condition();

	/**
	 * <p>Configures whether or not the first truthy condition should be
	 * exclusive.
	 * 
	 * <p>If {@code true}, then only the first truthy condition will be
	 * executed. If {@code false}, then all truthy conditions will be executed.
	 * The default value is {@code true}.
	 */
	boolean exclusive() default true;

	/**
	 * <p>The order of execution this annotation should be executed in, with
	 * respect to other conditional annotations that are also decorating this
	 * param.
	 */
	int order() default Event.DEFAULT_ORDER_NUMBER;

	/**
	 * <p>Whether or not to reset the state of the target field when the
	 * associated &#64;{@code Values} property is updated.
	 * 
	 * <p>If a condition is truthy and {@code resetOnChange} is {@code false},
	 * then state will be attempt to be preserved. State is preserved in this
	 * scenario only if the value of state is found within the newly updated
	 * {@code &#64;Values} property. If it is not found, the state will be
	 * reset.
	 * 
	 * <p>If {@code resetOnChange} is {@code true}, state will always be reset
	 * on change.
	 * 
	 * <p>The default value is {@code true}.
	 */
	boolean resetOnChange() default true;

	/**
	 * <p>The target path relative to the this annotated field to update.
	 */
	String targetPath();
}
