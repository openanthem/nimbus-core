/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model.core;

import java.util.Date;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 */
@Model
@Getter @Setter @EqualsAndHashCode(of={"service", "something", "discharge", "elemId"})
public class ServiceLine {
	@Ignore
	private static final long serialVersionUID = 1L;
	
	@Model
	@Getter @Setter @EqualsAndHashCode(of={"by", "why", "when"}) 
	public static class AuditInfo {
		private String by;
		private String why;
		private Date when;
	}
	
	@Model
	@Getter @Setter @EqualsAndHashCode(of={"yesNo", "audits"}) 
	public static class Discharge {
		private boolean yesNo;
		
		private List<AuditInfo> audits;
	}

	private String service;

	private String something;

	private Discharge discharge;
	
	private String elemId;
}
