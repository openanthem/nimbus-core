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

/**
 * <p> {@link StyleConditional} is an extension capability provided by the
 * framework. The annotation is used to conditionally set the style property on
 * the param represented by the decorated field based on a SpEL condition. The
 * Framework provides default event handling for this annotation during the
 * {@link OnStateLoad} and {@link OnStateChange} events.
 * 
 * <p> The following sample code shows how to configure a field using
 * {@link StyleConditional} by setting the style property of the param
 * represented by {@code nickname} based on the state of
 * {@code shouldUseNickname}.
 * 
 * <pre>
 * &#64;GridColumn
 * &#64;Path
 * private String nickname;
 * 
 * &#64;GridColumn(hidden = true)
 * &#64;Path
 * &#64;StyleConditional(targetPath = "/../nickname", condition = {
 * 		&#64;StyleConditional.Condition(when = "state == true", then = &#64;Style(cssClass = "preferred")) })
 * private boolean shouldUseNickname;
 * </pre>
 * 
 * <p>In this scenario, when {@code shouldUseNickname} is {@code true},
 * {@code nickname} will have the css class "preferred" added in the final HTML
 * rendering of the &#64;GridColumn component. For example, assume the value of
 * {@code shouldUseNickname} is {@code true} and the state of {@code nickname}
 * is {@code "Bob"}, then something similar the following would be rendered:
 * 
 * <pre>
 * &lt;span title="Bob" class="ng-star-inserted"&gt;
 *   &lt;span class="fieldValue nickname preferred"&gt;Bob&lt;/span&gt;
 * &lt;/span&gt;
 * </pre>
 * 
 * <p>Each view component has it's own rules that specify where in the HTML
 * structure the attributes contained within &#64;Style will be written to. See
 * the UI documentation or the final rendered HTML for more information.
 * 
 * <p> This annotation can be triggered for multiple events by providing one or
 * more {@link Condition} annotations within {@code targetPath} and/or multiple
 * {@link StyleConditional} annotations.
 * 
 * @author Tony Lopez
 * @since 1.1
 * @see com.antheminc.oss.nimbus.domain.defn.extension.StyleConditionals
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.StyleConditionalStateEventHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(StyleConditionals.class)
@OnStateChange
@OnStateLoad
public @interface StyleConditional {

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Conditions.class)
	public @interface Condition {

		/**
		 * <p>The {@link Style} object to assign to the param(s) identified by
		 * {@code targetPath} when this condition's {@link #when()} is
		 * {@code true}. <p><b>Applying more than one style
		 * class</b><br>Multiple css classes can be applied by providing a space
		 * delimited string for {@link Style#cssClass()}.
		 */
		Style then();

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
	 * <p>Path of param to apply styles to when condition is satisfied. The path
	 * is relative to param on which this annotation is declared.
	 */
	String targetPath();
}
