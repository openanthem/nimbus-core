/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.s3.core;

import java.util.Date;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Model;

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
