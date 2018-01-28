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
package com.anthem.oss.nimbus.core;

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
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.CommandMessageConverter;
import com.antheminc.oss.nimbus.entity.client.Client;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractFrameworkIntegrationTests {

	@Autowired protected WebActionController controller;
	
	@Autowired protected MongoOperations mongo;
	
	@Autowired protected MongoTemplate mt;
	
	@Autowired protected CommandMessageConverter converter;
	
	protected static final String CLIENT_ID = "hooli";
	
	protected static final String PLATFORM_ROOT = "/"+CLIENT_ID+"/thebox/p"; 
	
	
	@Before
	public void t0_init() {
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