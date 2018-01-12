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
package com.anthem.nimbus.platform.core.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.test.sample.um.model.core.Patient;
import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;

/**
 * @author Swetha Vemuri
 *
 */
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmbedMongoConnectionTest extends AbstractFrameworkIntegrationTests {
	
	@Autowired
	MongoOperations mongoOps;
	
	/*
	 * This test case is written to verify the connection of embedded mongodb.
	 */
	@Test
	public void tc01_createTestData() {
		UMCase umc = new UMCase();
		umc.setCaseType("medical");
		umc.setRequestType("outpatient");
		Patient p = new Patient();
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setSubscriberId("YZW12345");
		umc.setPatient(p);
		
		mongoOps.insert(umc, "test_umcase");
		assertTrue(mongoOps.collectionExists("test_umcase"));
		assertNotNull(mongoOps.findAll(UMCase.class, "test_umcase"));
		
		List<UMCase> dbList = mongoOps.findAll(UMCase.class, "test_umcase");
		assertNotNull(dbList.get(0));
		assertNotNull(dbList.get(0).getId());
	}
	
	/*
	 * This test case is written to verify the session of the embedded mongodb runs for multiple tests in the suite
	 */
	@Test
	public void tc02_createTestData() {
		UMCase umc = new UMCase();
		umc.setCaseType("surgical");
		umc.setRequestType("inpatient");
		Patient p = new Patient();
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setSubscriberId("YZW12345");
		umc.setPatient(p);
		
		mongoOps.insert(umc, "test_umcase_ip");
		assertTrue(mongoOps.collectionExists("test_umcase_ip"));
		assertNotNull(mongoOps.findAll(UMCase.class, "test_umcase_ip"));
		
		List<UMCase> dbList = mongoOps.findAll(UMCase.class, "test_umcase_ip");
		assertNotNull(dbList.get(0));
		assertNotNull(dbList.get(0).getId());
	}

}
