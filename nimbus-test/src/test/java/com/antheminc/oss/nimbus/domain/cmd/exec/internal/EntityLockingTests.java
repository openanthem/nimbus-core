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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

import org.bson.Document;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.session.TestSessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityLockingTests extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	private TestSessionProvider testSessionProvider;
	
	@Autowired
	private MongoOperations mongoOps;
	
	@Before
	public void setup() {
		MongoCollection<Document> collection  = mongoOps.createCollection("lockentry");
		IndexOptions indexOptions = new IndexOptions().unique(true);
		collection.createIndex(Indexes.ascending("alias", "refId"), indexOptions);		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void t01_create_session_lock() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/lockview")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/samplelockedentity")
				.addAction(Action._new)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request2, "{\"sourceParameter\":\"1\"}");
		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/lockview/lockStatus/_get")
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		assertEquals(response.getState(),"locked");
		
		testSessionProvider.clear();
		
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/samplelockedentity:1")
				.addAction(Action._get)
				.getMock();
		
		try {
			holder = (Holder<MultiOutput>)controller.handlePost(request4, null);
			fail("Lock exception should have occured");
		} catch (FrameworkRuntimeException e) {
			request3 = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/lockview/lockStatus/_get")
					.addAction(Action._get)
					.getMock();
			holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
			response = (Param<?>)holder.getState().getSingleResult();
			assertEquals(response.getState(),"unableToLock");

		}
		
	}
	


		
	
}
