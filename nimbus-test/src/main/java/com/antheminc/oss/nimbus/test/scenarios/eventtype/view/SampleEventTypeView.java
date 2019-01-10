package com.antheminc.oss.nimbus.test.scenarios.eventtype.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
import com.antheminc.oss.nimbus.test.domain.defn.extension.EventNoOverrides;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.ConflictingEventOverride;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.EventAllowOverride;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.EventMandatoryOverride;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.EventNoOverride;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.OnStateChangeNoOverride;
import com.antheminc.oss.nimbus.test.domain.defn.extension.TestEventType.RedundantEventOverride;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain("sampleeventtypeview")
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter @ToString
public class SampleEventTypeView extends IdLong {
	
	private static final long serialVersionUID = 1L;

	@EventNoOverrides({
		@EventNoOverride(value= "no override event annotation")
	})
	private String attr1;
	
	@EventAllowOverride(value = "override allowed event annotation", eventType= EventType.OnStateLoad)
	private String attr2;
	
	@OnStateChangeNoOverride(value = "no override on state change annotation")
	private String attr3;
	
	@ConflictingEventOverride(value = "conflicing event annotation")
	private String attr4;
	
	@RedundantEventOverride(value = "redundant event annotaiton")
	private String attr5;
	
	@EventMandatoryOverride(value = "mandatory override event annotation", eventType=EventType.OnStateLoad)
	private String attr6;
	
	
	
}
