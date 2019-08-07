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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;

/**
 * <p>
 * &#64;{@code ActivateConditional} is an extension capability provided by the
 * framework. The annotation is used to conditionally activate/deactivate the
 * param represented by the decorated field based on a SpEL condition. The
 * Framework provides default event handling for this annotation during the
 * {@code onStateChange} and {@code onStateLoad} events.
 * 
 * <p>
 * The following sample code shows how to configure the active property for
 * {@code sectionG} based on the state of {@code skipSectionG} is {@code true}.
 * 
 * <pre>
 * &#64;CheckBox(postEventOnChange = true)
 * &#64;ActivateConditional(when = "state != 'true'", targetPath = "/../sectionG")
 * private String skipSectionG;
 * 
 * private SectionG sectionG;
 * </pre>
 * 
 * <p>
 * This annotation can be triggered for multiple events by providing one or more
 * values for {@code targetPath} and/or multiple
 * &#64;{@code ActivateConditional} annotations.
 * 
 * <p>
 * The activate/deactivate logic will affect the enabled and visible properties.
 * <b>Deactivate will also affect state property of the decorated param by
 * setting the state to {@code null}</b>. If needing to control each of these
 * properties individually, consider using a different conditional annotation:
 * <ul>
 * <li>{@link EnableConditional}</li>
 * <li>{@link VisibleConditional}</li>
 * </ul>
 * 
 * <p>
 * For more explicit details on the activate/deactivate logic, see
 * {@link DefaultParamState#activate()} and
 * {@link DefaultParamState#deactivate()}.
 * 
 * @author Soham Chakravarti
 * @since 1.0
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ActivateConditionalStateEventHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ActivateConditionals.class)
@OnStateChange
@OnStateLoad
public @interface ActivateConditional {

	/**
	 * <p>SpEL based condition on which param would be inactivated. <br>
	 * If value is not overridden, then the negation of
	 * {@linkplain ActivateConditional#when()} would be used
	 */
	String inactivateWhen() default "";

	/**
	 * <p>The order of execution this annotation should be executed in, with
	 * respect to other conditional annotations that are also decorating this
	 * param.
	 */
	int order() default Event.DEFAULT_ORDER_NUMBER;

	/**
	 * <p>Path of param to activate when condition is satisfied relative to param
	 * on which this annotation is declared
	 */
	String[] targetPath();

	/**
	 * <p>SpEL based condition to be evaluated relative to param's state on which
	 * this annotation is declared.
	 */
	String when();
}
