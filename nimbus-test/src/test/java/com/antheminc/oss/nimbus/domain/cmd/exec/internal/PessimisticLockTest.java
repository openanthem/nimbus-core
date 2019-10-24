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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.LockEntity;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.TestModel;

/**
 * @author Sandeep Mantha
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PessimisticLockTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	SessionProvider sessionProvider;
	
	protected static final String ROOT_DOMAIN = PLATFORM_ROOT + "/rootdomain";

	@Test
	public void t01_domainLock() {
		ClientUser cu = new ClientUser();
		cu.setLoginId("user1");
		sessionProvider.setLoggedInUser(cu);
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Long refId = createOrGetDomainRoot_RefId();
		
		cu.setLoginId("user2");
		sessionProvider.setLoggedInUser(cu);
		
		try {
			createOrGetDomainRoot_RefId();
		} catch(Exception e) {
			assertEquals(e.getMessage(), "Domain is currently locked");
		}

	}
	
	@Test
	public void t02_domainLock() {
		ClientUser cu = new ClientUser();
		cu.setLoginId("user1");
		sessionProvider.setLoggedInUser(cu);
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Long refId = createOrGetDomainRoot_RefId();
		
		cu.setLoginId("user2");
		sessionProvider.setLoggedInUser(cu);
		
		MockHttpServletRequest nav_req = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._nav).getMock();
		Object nav_rsp = controller.handleGet(nav_req, null);
		assertNotNull(nav_rsp);
		
		List<LockEntity> lst = mongo.findAll(LockEntity.class);


	}
	
}

