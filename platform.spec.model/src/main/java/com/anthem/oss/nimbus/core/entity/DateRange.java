/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

import java.time.LocalDate;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Data
public class DateRange {

	private static final long serialVersionUID = 1L;
	
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDate start;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDate end;
	
}
