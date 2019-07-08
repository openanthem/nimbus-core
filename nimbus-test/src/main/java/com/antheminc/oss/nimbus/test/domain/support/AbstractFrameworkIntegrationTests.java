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
package com.antheminc.oss.nimbus.test.domain.support;

import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Soham Chakravarti
 * @author Tony Lopez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FrameworkIntegrationTestScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractFrameworkIntegrationTests extends AbstractFrameworkTest {

	@Autowired
	protected WebActionController controller;

	@Autowired
	protected MongoOperations mongo;

	@Autowired
	protected MongoTemplate mt;

	@Autowired
	protected CommandMessageConverter converter;

	@Autowired
	protected ObjectMapper om;

	protected JacksonTester<Object> json;

	@Before
	public void before() {
		this.tearDown();

		JacksonTester.initFields(this, om);

		Client newClient = new Client();
		Long id = new Random().nextLong();
		newClient.setId(id);
		newClient.setName(CLIENT_ID);
		mongo.insert(newClient, "cliententity");

		assertNotNull(mongo.findById(id, Client.class, "cliententity"));
	}

	/**
	 * Drop the test db "integrationtest"
	 */
	@After
	public void tearDown() {
		mt.getDb().drop();
	}

	protected String asString(Object o) {
		try {
			return this.om.writeValueAsString(o);
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Failed to convert object to string.", e);
		}
	}

	/**
	 * <p> Determine whether or not the entity exists within the system. Test
	 * classes may override this to assume different behavior.
	 * @param refId the id of the stored object
	 * @param entityClass the type of the object
	 * @param alias the domain alias of the object
	 * @return {@code} true if a domain entity exists, {@code false} otherwise
	 * @see #find(Long, Class, String)
	 */
	protected boolean exists(Long refId, Class<?> entityClass, String alias) {
		return null != find(refId, entityClass, alias);
	}

	/**
	 * <p> Find the entity within the system. Default implementation uses
	 * MongoDB for retrieval. Test classes may override this to assume different
	 * behavior.
	 * @param refId the id of the stored object
	 * @param entityClass the type of the object
	 * @param alias the domain alias of the object
	 * @return {@code} true if a domain entity exists, {@code false} otherwise
	 */
	protected <T> T find(Long refId, Class<T> entityClass, String alias) {
		return this.mongo.findById(refId, entityClass, alias);
	}

	/**
	 * <p> Inserts an entity into the system. Default implementation uses
	 * MongoDB to perform insertion. Test classes may override this to assume
	 * different behavior.
	 * @param alias the domain alias of the object
	 * @param o the object to insert
	 * @return the inserted object
	 */
	protected <T> T insert(String alias, T o) {
		this.mongo.insert(o, alias);
		return o;
	}
}