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
package com.antheminc.oss.nimbus.domain.defn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.Executions.DetourConfigs;
import com.antheminc.oss.nimbus.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
@Inherited
public @interface Execution {

	/**
	 * <p>
	 * &#64;{@code Config} is a core execution capability provided by the
	 * framework. The annotation is used to execute {@code CommandMessage}
	 * objects built from a provided Command DSL URLs, executed from the context
	 * of the param represented by the decorated field.
	 * 
	 * @author Soham Chakravarti
	 * @since 1.0
	 * @see com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultCommandExecutorGateway
	 * @see com.antheminc.oss.nimbus.domain.cmd.CommandMessage
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@Repeatable(Configs.class)
	@Execution
	public @interface Config {
		public static String COL = StringUtils.EMPTY;
		public static String TRUE = "true";

		/**
		 * <p>A path to a nested collection param, relative to the param
		 * represented by this decorated field.
		 * <p>When provided, instead of executing this &#64;{@code Config} from the
		 * context of the decorated field's param, it will execute on each of
		 * the collection elements belonging to the collection identified by
		 * {@code col}.
		 * <p>If {@code col} is not a path to a collection element, an
		 * {@link InvalidConfigException} will be thrown.
		 */
		String col() default COL;

		/**
		 * <p>The order of execution this annotation should be executed in, with
		 * respect to other conditional annotations that are also decorating
		 * this param.
		 */
		int order() default Event.DEFAULT_ORDER_NUMBER;

		/**
		 * <p>The Command DSL URL to execute.
		 */
		String url();

		/**
		 * <p>SpEL based condition to be evaluated relative to the param's state on
		 * which this annotation is declared.
		 * <p>When the condition is evaluated as {@code true}, this
		 * &#64;{@code Config} will be executed. If not provided, this
		 * &#64;{@code Config} will always execute.
		 */
		String when() default TRUE;
	}

	/**
	 * @author Rakesh Patel
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@Repeatable(DetourConfigs.class)
	@Execution
	public @interface DetourConfig {
		Config main();

		Config onException() default @Config(url = "");

		int order() default Event.DEFAULT_ORDER_NUMBER;
	}

}
