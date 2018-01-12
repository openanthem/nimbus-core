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
package com.anthem.oss.nimbus.test.sample.um.model.core;

import java.time.LocalDate;

import com.anthem.oss.nimbus.core.domain.definition.extension.DateRange;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter

/**
 * @author Sandeep Mantha
 *
 */

public class TestBean {

	@DateRange
	private LocalDate dt;
	
}
