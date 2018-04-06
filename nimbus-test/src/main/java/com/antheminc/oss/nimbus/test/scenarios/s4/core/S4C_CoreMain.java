/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s4.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Sample core entity to illustrate mappings for:<br />
 * <pre>Transient Collection Element </pre>
 * <pre>Transient Nested Model </pre>
 * 
 * @author Soham Chakravarti
 */
@SuppressWarnings("serial")
@Domain(value="s4c_main", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString
public class S4C_CoreMain extends IdLong {

	private List<S4C_AnotherModel> anotherModeList;
	
	//private S4C_AnotherModel anotherModelNested;
	
	@Config(url="/targetParam/_update?rawPayload=\"v1\"")
	@Config(url="/targetParam/_update?rawPayload=\"v2\"")
	private String events_output;
	
	private String targetParam;
}
