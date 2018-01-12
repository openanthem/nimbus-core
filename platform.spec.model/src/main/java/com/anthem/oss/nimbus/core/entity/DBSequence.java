/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.core.entity;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity to generate and track global unique id for MongoDB
 * TODO - currently using long for the id type, need to be BigInteger or similar
 * 
 * @author AC67870
 *
 */
@Domain(value="sequence")
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class DBSequence {

	@Id
	private String id;

	private long seq;

}
