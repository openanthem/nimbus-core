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
package com.antheminc.oss.nimbus.domain.defn.validaton;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder;

import lombok.RequiredArgsConstructor;

/**
 * <p>Validator for {@link com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder}
 * 
 * @author Tony Lopez
 * @since 1.3
 */
@RequiredArgsConstructor
public class ConfigPlaceholderValidator {

	private final ReservedKeywordRegistry reservedKeywordRegistry;
	
	public boolean supports(Class<?> clazz) {
		return ConfigPlaceholder.class.isAssignableFrom(clazz);
	}

	public void validate(Object target) {
		ConfigPlaceholder obj = (ConfigPlaceholder) target;
		if (this.reservedKeywordRegistry.exists(obj.name())) {
			throw new InvalidConfigException("The placeholder \"" + obj.name()
					+ "\" is a reserved keyword. Replacing reserved placeholders is not allowed.");
		}
	}

}
