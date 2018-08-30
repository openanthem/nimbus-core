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
		
		String value() default "";
		
		String helpText() default "";
		
		/**
		 * IETF BCP 47 language tag representing this locale
		 */
		String localeLanguageTag() default "";
	}

	public static Locale getLocale(Label label) {
		return Optional.ofNullable(label.localeLanguageTag())
				.filter(StringUtils::isNotEmpty)
				.map(Locale::forLanguageTag)
				.orElse(Locale.getDefault());
	}

}
