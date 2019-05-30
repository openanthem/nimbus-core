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
/**
 * 
 */
package com.antheminc.oss.nimbus.domain.defn.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.annotation.Annotation;
import java.util.Locale;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentTest {

	public static Label create(String val, String langTag) {
		return create(val, langTag, null, "");
	}
	
	public static Label create(String val, String langTag, String helpText, String cssClass) {
		return new Label() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Label.class;
			}
			@Override
			public String value() {
				return val;
			}
			@Override
			public String helpText() {
				return helpText;
			}
			@Override
			public String localeLanguageTag() {
				return langTag;
			}
			public Style style() {
				return new Style() {
					@Override
					public Class<? extends Annotation> annotationType() {
						return Style.class;
					}
					@Override
					public String cssClass() {
						return cssClass;
					}
					
				};
			}
		};
	
	}
	
	@Test
	public void t01_label_locale_default() {
		Label l = create("TEST", Label.DEFAULT_LOCALE_LANGUAGE_TAG);
		assertSame(Locale.getDefault(), Content.getLocale(l));
	}
	
	@Test
	public void t02_label_locale_french() {
		Label l = create("TEST", "fr");
		assertSame(Locale.FRENCH, Content.getLocale(l));
	}
	
	@Test
	public void t03_label_locale_en() {
		Label l = create("TEST", "en");
		assertSame(Locale.ENGLISH, Content.getLocale(l));
	}
	
	@Test
	public void t04_label_locale_en_us() {
		Label l = create("TEST", "en-US");
		assertSame(Locale.US, Content.getLocale(l));
	}

	@Test
	public void t05_label_locale_en_us_ignoreCase() {
		Label l = create("TEST", "EN-us");
		assertSame(Locale.US, Content.getLocale(l));
	}

	@Test
	public void t06_label_locale_custom() {
		Label l = create("TEST", "hi-in");
		assertEquals(new Locale("hi", "IN"), Content.getLocale(l));
	}

	@Test
	public void t07_label_locale_null() {
		Label l = create("TEST", null);
		assertEquals(Locale.getDefault(), Content.getLocale(l));
	}

	@Test
	public void t08_label_locale_empty() {
		Label l = create("TEST", "");
		assertEquals(Locale.getDefault(), Content.getLocale(l));
	}
}
