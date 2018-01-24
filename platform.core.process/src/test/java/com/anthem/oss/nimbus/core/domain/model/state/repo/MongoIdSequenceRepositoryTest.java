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
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertNotEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoIdSequenceRepositoryTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	IdSequenceRepository seqIdRepo;
	
	@Autowired
	MongoOperations mongoOps;
	
	@Test
	public void t1_testSequenceCollectionUpsert() {
		mongoOps.dropCollection("sequence");
		assertNotEquals(0, seqIdRepo.getNextSequenceId("global"));
		assertNotEquals(1, seqIdRepo.getNextSequenceId("global"));
	}
}
