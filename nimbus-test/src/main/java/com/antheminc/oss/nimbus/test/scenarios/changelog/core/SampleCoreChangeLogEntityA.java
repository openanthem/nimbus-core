/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.changelog.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="samplechangelogcore_a", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter
public class SampleCoreChangeLogEntityA extends IdLong {

	private static final long serialVersionUID = 1L;
	
	private String status;
	
	private String statusSetViaRule;
	
	private SampleCoreChangLogEntityNested sampleCoreChangLogEntityNested;
	
	@Config(url="/p/samplechangelogcore_b/_new?fn=_intiEntity&target=/.m/status&json=\"Test_Status_B\"&target=/.m/sampleCoreChangLogEntityNestedB/nestedStatus&json=\"Test_Nested_Status_B\"")
	private String action_createEntityB;
	
	@Model
	@Getter @Setter
	public static class SampleCoreChangLogEntityNested {
		
		private String nestedStatus;
		
	}
	
}
