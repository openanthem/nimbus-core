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
		return ChronoUnit.YEARS.between(birthdate, now);
	}

	@Override
	public LocalDate deserialize(Long age) {
		// This method is not required. We will not convert Age to DOB.
		return null;
	}

}
