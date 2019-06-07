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
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

/**
 * This annotation is used to provide the access restriction: <br> e.g. <p>1.
 * Below config specifies that if a user has role1, then only read permission is
 * allowed for testParam.
 * 
 * <pre>
 * &#64;AccessConditional(containsRoles = { "role1" }, p = Permission.READ)
 * String testParam;
 * </pre>
 * 
 * <p>2. Below config specifies that if a user has role1 or role2, then
 * testParam will be hidden.
 * 
 * <pre>
 * &#64;AccessConditional(containsRoles = { "role1", "role2" }, p = Permission.HIDDEN)
 * String testParam;
 * </pre>
 * 
 * <p>3. Below config specifies (using when()) that if a user has role1 but not
 * role2, only then testParam will be readOnly.
 * 
 * <pre>
 * &#64;AccessConditional(when='!?[#this == 'role1'].empty && ?[#this == 'role'].empty', p = Permission.READ)
 * String testParam;
 * </pre>
 * 
 * @author Rakesh Patel
 * @since 1.0
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.AccessConditionalStateEventHandler
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(AccessConditionals.class)
@OnStateLoad
public @interface AccessConditional {

	/**
	 * <p>A list of available permissions.
	 * 
	 * @author Rakesh Patel
	 * @since 1.0
	 */
	public enum Permission {
		HIDDEN, READ, WRITE;
	}

	/**
	 * <p>Use for simple area/accessEntity contains check, for complex
	 * expression, use {@link when()}.
	 */
	String[] containsAuthority() default {};

	/**
	 * <p>Use for simple role contains check, for complex expression, use
	 * {@link #when()}.
	 */
	String[] containsRoles() default {};

	/**
	 * <p>The order of execution this annotation should be executed in, with
	 * respect to other conditional annotations that are also decorating this
	 * param.
	 */
	int order() default Event.DEFAULT_ORDER_NUMBER;

	/**
	 * <p>The {@link Permission} should is required to enable access.
	 */
	Permission p();

	/**
	 * <p>Use this to specify an expression for authorities.
	 */
	String whenAuthorities() default "";

	/**
	 * <p>Use this to specify an expression for roles.
	 */
	String whenRoles() default "";
}
