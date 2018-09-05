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
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;

/**
 * <p> {@link LabelConditional} is an extension capability provided by the
 * framework. The annotation is used to conditionally set the label property on
 * the param represented by the decorated field based on a SpEL condition. The
 * Framework provides default event handling for this annotation during the
 * {@link OnStateLoad} and {@link OnStateChange} events.
 * 
 * <p> The following sample code shows how to configure a field using
 * {@link LabelConditional} by setting the label properties of the param
 * represented by {@code input2} based on the state of {@code input1}.
 * 
 * <pre>
 * &#64;Label("Enter the value 'TEST' to change the label for input2 below")
 * &#64;TextBox(postEventOnChange = true)
 * &#64;LabelConditional(targetPath = "/../input2", condition = {
 * 		&#64;Condition(when = "state != 'TEST'", then = &#64;Label("New Input 2 Label")) })
 * private String input1;
 * 
 * &#64;Label("Input 2")
 * private String input2;
 * </pre>
 * 
 * <p>In this scenario, {@code input2} will display a default label value of
 * {@code "Input 2"} on page load. When the state of {@code input1} is changed
 * to {@code "TEST"}, the label displayed for {@code input2} will read as
 * {@code "New Input 2 Label"}.
 * 
 * <p>Multiple labels can be provided to the {@link Condition#then()} attribute
 * to support internationalization.
 * 
 * <p> This annotation can be triggered for multiple events by providing one or
 * more {@link Condition} annotations within {@code targetPath} and/or multiple
 * {@link LabelConditional} annotations.
 * 
 * @author Tony Lopez
 * @since 1.1
 * @see com.antheminc.oss.nimbus.domain.defn.extension.LabelConditionals
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.LabelConditionalStateEventHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(LabelConditionals.class)
@OnStateChange
@OnStateLoad
public @interface LabelConditional {

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Conditions.class)
	public @interface Condition {

		/**
		 * <p>The {@link Label} object(s) to assign to the param(s) identified
		 * by {@code targetPath} when this condition's {@link #when()} is
		 * {@code true}.
		 */
		Label[] then();

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
	 * <p>Path of param to enable when condition is satisfied relative to param
	 * on which this annotation is declared
	 */
	String targetPath();
}
