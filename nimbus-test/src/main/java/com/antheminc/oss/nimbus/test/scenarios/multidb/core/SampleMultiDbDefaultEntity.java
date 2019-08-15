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
package com.antheminc.oss.nimbus.test.scenarios.multidb.core;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Tony Lopez
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Domain(value = "sample_multidb_default", includeListeners = { ListenerType.persistence })
@Repo(value = Database.rep_mongodb)
public class SampleMultiDbDefaultEntity {

		@Ignore
		private final String _class = this.getClass().getName(); 
		@Id
		private Long id;
		private String field1;
		private String field2;
		
		public SampleMultiDbDefaultEntity() {
			
		}
		
		public SampleMultiDbDefaultEntity(Long id, String primaryField1, String primaryField2) {
			this.id = id;
			this.field1 = primaryField1;
			this.field2 = primaryField2;
		}
	}
