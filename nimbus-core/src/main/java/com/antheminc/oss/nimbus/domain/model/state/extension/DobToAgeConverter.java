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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Dinakar.Meda
 *
 */
@EnableLoggingInterceptor
public class DobToAgeConverter implements ParamConverter<LocalDate, Long> {
	public static final String PREFIX = "ANT";
	
	@Override
	public Long serialize(LocalDate birthdate) {
		LocalDateTime now = LocalDateTime.now();
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
