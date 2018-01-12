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
package com.anthem.oss.nimbus.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.domain.model.state.internal.DobToAgeConverter;
import com.anthem.oss.nimbus.core.domain.model.state.internal.IdParamConverter;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StaticCodeValueBasedCodeToLabelConverter;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
public class DefaultModelConfiguration {

	@Bean
	public IdParamConverter idParamConverter(){
		return new IdParamConverter();
	}
	
	@Bean
	public DobToAgeConverter dobToAgeConverter(){
		return new DobToAgeConverter();
	}
	
	@Bean
	public StaticCodeValueBasedCodeToLabelConverter staticCodeValueBasedCodeToLabelConverter(){
		return new StaticCodeValueBasedCodeToLabelConverter();
	}
}
