/**
 * 
 */
package com.antheminc.oss.nimbus.domain.bpm;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.Lifecycle;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="testbpmfailmodel", includeListeners={ListenerType.persistence, ListenerType.update}) 
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
@Lifecycle(name="testbpmfailmodel")
public class TestBPMFailModel extends IdLong {
	private static final long serialVersionUID = 1L;
	private String parameter1;
	private String parameter2;
	private String parameter3;
	
	@Config(url="/p/invalidentityzzz/_new")
	private String action_fail;
}

