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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Model {
	
	/**
	 * alias
	 */
	String value() default "";
	
	String rules() default "drools";
	
	// list of listeners to exclude at a nested model level. Use only to exclude a particular listener defined at a root Domain level
	ListenerType[] excludeListeners() default { };
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Param {
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		@OnStateLoad
		public @interface Values {
			
			public static interface Source {
				public List<ParamValue> getValues(String paramCode);
			}
			
			public static class EMPTY implements Source {
				@Override
				public List<ParamValue> getValues(String paramPath) {
					return null;
				}
			}
			
			Class<? extends Source> value() default EMPTY.class;
			
			String url() default "staticCodeValue";
			
			/**
			 * <p>When {@code true}, attempts to retrieve
			 * {@code "staticCodeValue"} calls made with {@link #url()} from the
			 * "staticcodevalues" cache. <p>Note: There are other layers of
			 * caching defined within the {@link Command} execution lifecycle,
			 * but enabling this property will result in a more aggressive cache
			 * retrieval strategy by fetching from the cache before the
			 * {@link Command} has a chance to execute.
			 */
			boolean useParamValuesCacheOnLoad() default true;
		}

	}
	
}
