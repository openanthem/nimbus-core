package com.anthem.oss.nimbus.test.sample.um.model;

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
