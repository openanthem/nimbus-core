/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model.core;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
//@CoreDomain(value="patient") 
public class PatientSearchCriteria {

	private String subscriberId;

	private String firstName;

	private String lastName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
}
