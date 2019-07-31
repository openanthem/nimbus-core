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
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.EntityStateHolder;

/**
 * <p>
 * &#64;{@code ExpressionConditional} is an extension capability provided by the
 * framework. The annotation is used to conditionally apply logic using SpEL
 * commands based on a SpEL condition. The Framework provides default event
 * handling for this annotation during the {@code onStateChange} and
 * {@code onStateLoad} events.
 * 
 * <p>
 * The following sample code shows how to configure a field using
 * &#64;{@code ExpressionConditional} by setting the state of the param
 * represented by {@code initDateOnLoad} based only on initial loading of the
 * param.
 * 
 * <pre>
 * &#64;ExpressionConditional(when = "null == state && onLoad()", then = "setState(T(java.time.LocalDate).now())")
 * private LocalDate initDateOnLoad;
 * </pre>
 * 
 * <p>
 * Given that &#64;{@code ExpressionConditional} is extremely versatile, misuse
 * of the annotation could result in unexpected behavior from within the
 * framework. As such, the access to the context param in the {@code then}
 * condition has been restricted to provide access to a subset of it’s methods.
 * The methods available to be used can be identified by reviewing
 * {@link EntityStateHolder}.
 * 
 * <p>
 * This annotation can be triggered for multiple events by providing one or more
 * &#64;{@code ExpressionConditional} annotations.
 * 
 * @author Soham Chakravarti
 * @since 1.0
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ExpressionConditionalStateEventHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ExpressionConditionals.class)
@OnStateLoad
@OnStateChange
public @interface ExpressionConditional {

	/**
	 * <p>The order of execution this annotation should be executed in, with
	 * respect to other conditional annotations that are also decorating this
	 * param.
	 */
	int order() default Event.DEFAULT_ORDER_NUMBER;

	/**
	 * <p>SpEL based expression to be executed relative to param on which this
	 * annotation is declared, if {@link #when()} is true.
	 */
	String then();

	/**
	 * <p>SpEL based condition to be evaluated relative to param's state on which
	 * this annotation is declared.
	 */
	String when();
}
