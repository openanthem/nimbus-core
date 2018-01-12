/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;

/**
 * @author Dinakar.Meda
 *
 */
public class DobToAgeConverter implements ParamConverter<LocalDate, Long> {
	public static final String PREFIX = "ANT";
	
	@Override
	public Long serialize(LocalDate birthdate) {
		LocalDate now = LocalDate.now();
		if (birthdate != null) {
			return ChronoUnit.YEARS.between(birthdate, now);
		} else {
			return new Long(0);
		}
	}

	@Override
	public LocalDate deserialize(Long age) {
		// This method is not required. We will not convert Age to DOB.
		return null;
	}

}
