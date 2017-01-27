/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
public class DateRange implements Model {

	private static final long serialVersionUID = 1L;
	
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDate start;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDate end;
	
}
