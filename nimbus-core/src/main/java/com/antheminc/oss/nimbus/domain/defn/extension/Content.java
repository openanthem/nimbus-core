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
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.defn.extension.Contents.Labels;

/**
 * @author Soham Chakravarti
 *
 */
public final class Content {

	/**
	 * <p>Label is used to display label text in coordination with the
	 * components within {@link ViewConfig}.
	 * 
	 * <p>Each UI component has it's own specification for how {@code Label}
	 * will be applied. For more information on a specific implementation,
	 * please refer to the corresponding component of interest within
	 * {@link ViewConfig}. If specific details are not mentioned, one can assume
	 * the label behaves in accordance with the default label behavior.
	 * 
	 * <p><b>Example Usage</b><br>
	 * 
	 * <pre>
	 * &#64;Label("First Name:")
	 * &#64;TextBox
	 * private String firstName;
	 * </pre>
	 * 
	 * <p><b>Text Support</b><br> Label provides support for more than just
	 * plain text. Please see {@link #value()} for more details.
	 * 
	 * <p><b>Internationalization</b><br>Locale specific implementations can be
	 * provided by specifying a {@link #localeLanguageTag()} for each label
	 * decoration on a field. It is important to note that <b>only one label
	 * with a given locale is</b> allowed to be present at a time.
	 * 
	 * <pre>
	 * &#64;Label(value = "Last Name:", localeLanguageTag = "en-US")
	 * &#64;Label(value = "Apellido:", localeLanguageTag = "es_ES")
	 * &#64;TextBox
	 * private String lastName;
	 * </pre>
	 * 
	 * <p>See {@link #localeLanguageTag()} for more details.
	 * 
	 * <p><b>Conditional Labels</b><br>If conditional labels are needed,
	 * consider using {@link LabelConditional}.
	 * @author Tony Lopez
	 *
	 */
	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Labels.class)
	@OnStateLoad
	public @interface Label {

		public static final String DEFAULT_LOCALE_LANGUAGE_TAG = Locale.getDefault().toLanguageTag();

		/**
		 * <p>Defines custom style attributes for this label component.
		 */
		Style style() default @Style();

		/**
		 * <p>The label text to display. <p><b>Text Support</b><br> Label
		 * provides support for the following text types: <ul>
		 * <li>Plaintext</li> <li>Spring Expression Language (SpEL) text</li>
		 * <li>{@link Command} DSL path placeholders</li></ul>
		 * <p><i>Plaintext</i><br>
		 * <pre>
		 * &#64;Label(value = "First Name:")
		 * &#64;TextBox
		 * private String firstName;
		 * </pre>
		 * <p><i>Spring Expression Language (SpEL)</i><br>
		 * <pre>
		 * &#64;Label(value = "${env.labels.common.firstName}")
		 * &#64;TextBox
		 * private String firstName;
		 * </pre>
		 * <p>Full support for SpEL is allowed here, which goes beyond simple
		 * property placeholder evaluation. See <a
		 * href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">Spring
		 * Expression Language (SpEL)</a> for more information. <p><i>Command
		 * DSL Path Placeholders</i><br>
		 * <pre>
		 * &#64;Label(value = "Hello <&#33;/../firstName&#33;>!")
		 * &#64;TextBox
		 * private String greeting;
		 * </pre>
		 */
		String value() default "";

		String helpText() default "";

		/**
		 * <p>IETF BCP 47 language tag representing this locale
		 */
		String localeLanguageTag() default "";
	}

	public static Locale getLocale(Label label) {
		return Optional.ofNullable(label.localeLanguageTag()).filter(StringUtils::isNotEmpty)
				.map(Locale::forLanguageTag).orElse(Locale.getDefault());
	}

}
