package com.antheminc.oss.nimbus.core.entity.user;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Niranjan Konda
 *
 */

@Domain("clientuserstatus")
@Getter @Setter
public class UserStatus {
	
	private String status;	
	private String statusReason;
	private LocalDate effectiveDate;	
	private LocalDate terminationDate;
}
