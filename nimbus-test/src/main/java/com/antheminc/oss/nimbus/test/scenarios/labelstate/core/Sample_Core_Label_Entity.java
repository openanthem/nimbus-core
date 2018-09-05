/**
 *  Copyright 2016-2018 the original author or authors.
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
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
@Domain(value="sample_core_label",includeListeners = { ListenerType.persistence }) 
@Repo(alias = "sample_core_label", value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter @Setter @ToString
public class Sample_Core_Label_Entity extends IdLong {
	
	private static final long serialVersionUID = 1L;

	private String attr1;
	
	@Label("Test Label en")
	private String attr2;
	
	@Label("Test label nested en")
	private Nested_Attr attr_nested;
	
	private List<String> str_coll_attr;
	
	@Label("Test label coll")
	private List<Nested_Attr> nested_coll_attr;	
	
	@Label("Test label coll en")
	@Label(value="Test label coll fr",localeLanguageTag="fr")
	private List<Nested_Attr> nested_coll_attr2;
	
	@Model @Getter @Setter
	public static class Nested_Attr {
		@Label("Test label nested attr")
		private String attr_nested_1;
		
		@Label("Test label nested level 2")
		private Nested_Attr_Level2 attr_nested_2;
		
	}
	
	@Model @Getter @Setter
	public static class Nested_Attr_Level2 {
		@Label("Test label nested level 2 attr")
		private String attr_nested_level_2;
	
	}
}
