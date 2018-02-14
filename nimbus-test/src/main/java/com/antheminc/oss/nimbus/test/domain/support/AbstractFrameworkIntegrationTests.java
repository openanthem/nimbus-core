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
package com.antheminc.oss.nimbus.test.domain.support;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrameworkIntegrationTestScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractFrameworkIntegrationTests {
	
	protected static final String CLIENT_ID = "hooli";
	
	protected static final String PLATFORM_ROOT = "/"+CLIENT_ID+"/thebox/p";
	
	protected static final String SAMPLE_CORE_ENTITY_ACCESS_ALIAS = "sample_core_access";
	protected static final String CLIENT_USER_ALIAS = "clientuser";
	protected static final String CLIENT_USER_GROUP_ALIAS = "clientusergroup";
	protected static final String QUEUE_ALIAS = "queue";
	protected static final String STATIC_CODE_VALUE_ALIAS = "staticCodeValue";
	
	
	
	@Autowired protected WebActionController controller;
	
	@Autowired protected MongoOperations mongo;
	
	@Autowired protected MongoTemplate mt;
	
	@Autowired protected CommandMessageConverter converter;
	
	
	
	
	@Before
	public void before() {
		this.tearDown();
		Client newClient = new Client();
		newClient.setId(CLIENT_ID);
		mongo.insert(newClient, "cliententity");
		
		assertNotNull(mongo.findById(CLIENT_ID, Client.class, "cliententity"));
	}
	
	/**
	 * Drop the test db "integrationtest" 
	 */
	@After
	public void tearDown() {
		mt.getDb().dropDatabase();
	}
}