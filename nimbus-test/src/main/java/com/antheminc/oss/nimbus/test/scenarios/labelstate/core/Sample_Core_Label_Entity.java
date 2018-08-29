/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.labelstate.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AC63348
 *
 */
@Domain(value="sample_core_label",includeListeners = { ListenerType.persistence }) 
@Repo(alias = "sample_core_label", value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter @Setter @ToString
public class Sample_Core_Label_Entity {
	
	private String attr1;
	
	private Nested_Attr attr_nested;
	
	private List<String> str_coll_attr;
	
	private List<Nested_Attr> nested_coll_attr;
	
	@Model @Getter @Setter
	public static class Nested_Attr {
		
		private String attr_nested_1;
	}
}
