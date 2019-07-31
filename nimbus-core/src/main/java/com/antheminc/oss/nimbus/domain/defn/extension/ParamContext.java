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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

/**
 * <p>ParamContext is used to set the contextual properties of a field during the OnStateLoad event. 
 * Contextual properties include those such as: visible, enabled, etc. It is likely that as the framework
 * evolves, additional values will be defined within <tt>ParamContext</tt>.
 * 
 * <p>The intent of <tt>&#64;ParamContext</tt> is to be able to decorate fields with default contextual
 * behavior. For example:</p>
 * 
 * <pre>
 * public static class SampleView {
 * 
 *     &#64;Modal(context = &#64;ParamContext(enabled=true, visible=true))
 *     private MyModal myModal;
 *     
 *     public static class MyModal { ... }
 * }
 * </pre>
 * 
 * <p>In this scenario we have configured the contextual values for enabled and visible to be
 * <b>true</b>. These values will be set during the <tt>OnStateLoad</tt> event.</p>
 * 
 * @author Soham Chakravarti, Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ParamContextStateEventHandler
 *
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE})
@OnStateLoad
public @interface ParamContext {

	/**
	 * Whether or not the decorated target should be visible.
	 */
	boolean visible();
	
	/**
	 * Whether or not the decorated target should be enabled.
	 */
	boolean enabled();
	
	int order() default Event.DEFAULT_ORDER_NUMBER;
	
}
