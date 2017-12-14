package com.antheminc.oss.nimbus.test.sample.um.model;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.core.domain.definition.extension.DateRange;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */

@Getter @Setter
public class TestBean {

	@DateRange
	private LocalDate dt;
	
}