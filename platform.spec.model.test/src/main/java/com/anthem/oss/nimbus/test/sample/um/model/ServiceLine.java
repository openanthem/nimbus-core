/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model;

import java.util.Date;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature.Ignore;
import com.anthem.nimbus.platform.spec.model.dsl.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 */
@Getter @Setter
public class ServiceLine extends AbstractModel.IdString {
	@Ignore
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter @Model
	public static class AuditInfo {
		private String by;
		private String why;
		private Date when;
	}
	
	@Getter @Setter @Model
	public static class Discharge {
		private boolean yesNo;
		
		private List<AuditInfo> audits;
	}

	private String service;

	private String something;

	private Discharge discharge;
}
