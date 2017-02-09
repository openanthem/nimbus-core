/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
@Domain(value="umcase") 
@Execution.Input(Action._search) 
@Execution.Output(value=Action._search, paginated=true) 
public class UMCaseSearchCriteria {

	@NotNull
	private String caseId;

	private String firstName;

	private String lastName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDate dob;
	
}
