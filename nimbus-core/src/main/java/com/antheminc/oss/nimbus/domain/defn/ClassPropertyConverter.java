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
package com.antheminc.oss.nimbus.domain.defn;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;

import com.antheminc.oss.nimbus.support.JustLogit;
/**
 * PropertyBinding for converting string values to Class<?>.
 * Converts string values in application.yml to Class<?><br> for the properties that are mapped to converted type.
 * 
 * Ex:
 * <pre>
 * private Map<Class<?>,String> exceptions;
 * 
 * application:
 * 	exceptions: 
 * 		com.antheminc.oss.nimbus.InvalidConfigException: System configuration error. 
 * </pre>
 * 
 *
 * @author Swetha Vemuri
 *
 */
@ConfigurationPropertiesBinding
public class ClassPropertyConverter implements Converter<String,Class<?>>{

	private JustLogit logit = new JustLogit(this.getClass());
	
	@Override
	public Class<?> convert(String source) {
		try {
			return ClassUtils.getClass(source, false);
		} catch (ClassNotFoundException e) {
			logit.error(() -> "Cannot convert config property value: ["+source+"] into a valid class");
		}
		return null;
	}

}
