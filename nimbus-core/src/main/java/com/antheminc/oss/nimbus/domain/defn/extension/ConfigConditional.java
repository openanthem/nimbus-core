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

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;

/**
 * <p>
 * &#64;{@code ConfigConditional} is an extension capability provided by the
 * framework. The annotation is used to conditionally execute
 * &#64;{@code Config} statements based on a SpEL condition. The param context
 * from which each &#64;{@code Config} will be exectued will be from the param
 * represented by the decorated field. The Framework provides default event
 * handling for this annotation during the {@code onStateChange} event.
 * 
 * <p>
 * The following sample code shows how to configure a field using
 * &#64;{@code ConfigConditional} by defining &#64;{@code Config} statements
 * that will be executed based on the state of {@code status}.
 * 
 * <pre>
 * &#64;ConfigConditional(when = "state == 'Completed'", config = {
 * 	&#64;Config(url = "<!#this!>/../state/_update?rawpayload=\"Closed\""),
 * 	&#64;Config(url = "/p/dashboard/_get")
 * })
 * 
 * private String status;
 * </pre>
 * 
 * <p>
 * This annotation can be triggered for multiple events by providing one or more
 * &#64;{@code ConfigConditional} annotations.
 * 
 * @author Soham Chakravarti
 * @since 1.0
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ConfigConditionalStateChangeHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ConfigConditionals.class)
@OnStateChange
public @interface ConfigConditional {

	/**
	 * <p>An array of &#64;{@code Config} statements to execute when this
	 * annotation's {@link #when()} condition is satisfied.
	 */
	public Config[] config();

	/**
	 * <p>SpEL based condition to be evaluated relative to the param's state on
	 * which this annotation is declared.
	 */
	public String when() default "true";
}
