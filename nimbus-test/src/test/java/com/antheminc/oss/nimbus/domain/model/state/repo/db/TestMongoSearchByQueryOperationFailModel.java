/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Domain(value="testmongosearchbyqueryoperationfailmodel", includeListeners={ListenerType.persistence, ListenerType.update},lifecycle="testmongosearchbyqueryoperationfailmodel") 
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class TestMongoSearchByQueryOperationFailModel extends IdLong {
	private static final long serialVersionUID = 1L;
	private String parameter1;
	private String parameter2;
	private String parameter3;
	
	@Config(url = "<!#this!>/.m/_process?fn=_set&url=/p/covid19/_search?fn=query&collation={\"locale\":\"en\",\"strength\":2}&where="
			+ " {aggregate: 'covid19'," + " pipeline: [{ $match: {continent: \"Asia\"}},\n"
			+ "{$sort:{country: -1} }]}")
	private String action_fail;
}

