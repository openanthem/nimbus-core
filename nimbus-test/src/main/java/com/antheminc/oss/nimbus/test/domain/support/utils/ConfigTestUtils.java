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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional.Condition;

/**
 * @author Tony Lopez
 *
 */
public class ConfigTestUtils {

	public static Values createValues(Class<? extends Source> value, String url) {
		return new Values() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Values.class;
			}

			@Override
			public Class<? extends Source> value() {
				return value;
			}

			@Override
			public String url() {
				return url;
			}
			
		};
	}
	
	public static ValuesConditional.Condition createValuesConditionalCondition(String when, Values then) {
		return new Condition() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Condition.class;
			}

			@Override
			public Values then() {
				return then;
			}

			@Override
			public String when() {
				return when;
			}
			
		};
	}
	
	public static ValuesConditional createValuesConditional(Condition[] condition, boolean exclusive, int order, boolean resetOnChange, String targetPath) {
		return new ValuesConditional() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return ValuesConditional.class;
			}

			@Override
			public Condition[] condition() {
				return condition;
			}

			@Override
			public boolean exclusive() {
				return exclusive;
			}

			@Override
			public int order() {
				return order;
			}

			@Override
			public boolean resetOnChange() {
				return resetOnChange;
			}

			@Override
			public String targetPath() {
				return targetPath;
			}
			
		};
	}
}
