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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.LockEntity;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Sandeep Mantha
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PessimisticLockTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	SessionProvider sessionProvider;
	
	protected static final String ROOT_DOMAIN = PLATFORM_ROOT + "/rootdomain";
	
	protected static final String SAMPLE_LANDING_VIEW = PLATFORM_ROOT + "/samplelandingview";
	
	// 1. _new on view 2. _update on view 3. mock another session and _get on refId
	@Test
	public void t01_domainLock() {
		setSession("user1");
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));
		
		try {
			sessionProvider.clear();
			Query query = new Query(new Criteria("sessionId").is(sessionProvider.getSessionId()));
			Update update = new Update().set("sessionId", "session2");
			mongo.updateFirst(query, update, "lock");
			MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addRefId(refId).addAction(Action._get).getMock();
			Object resp2 = controller.handleGet(req2, null);
			assertNotNull(home_newResp);
		} catch(FrameworkRuntimeException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	//1. _new on view 2. _update on view 3. _nav on view
	@Test
	public void t02_domainLock() {
		setSession("user1");
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));
		
		MockHttpServletRequest nav_req = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addRefId(refId).addAction(Action._nav).
										addParam(Constants.KEY_NAV_ARG_PAGE_ID.code, "vpPage2").getMock();
		Object nav_rsp = controller.handleGet(nav_req, null);
		assertNotNull(nav_rsp);
		
		List<LockEntity> lst = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst.size(), 1);

	}

	//1. _new on view without mapsTo model 2. _get on a grid (model lockable) 3. mock new session 4._new on view without mapsTo model and _get on grid
	@Test
	public void t03_domainLock() {
		setSession("user1");
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		MockHttpServletRequest home_newReq1 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).addAction(Action._new).getMock();
		Object home_resp1 = controller.handleGet(home_newReq1, null);
		assertNotNull(home_resp1);
		
		MockHttpServletRequest new_req1 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).
				addNested("/vpTest/vtTest/vsTestSearch/testgrid").addAction(Action._get).getMock();
		Object new_resp1 = controller.handleGet(new_req1, null);
		
		sessionProvider.clear();
		Query query = new Query(new Criteria("sessionId").is(sessionProvider.getSessionId()));
		Update update = new Update().set("sessionId", "session2");
		mongo.updateFirst(query, update, "lock");
		
		List<LockEntity> lst = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst.size(), 1);
		
		MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).addAction(Action._new).getMock();
		Object resp2 = controller.handleGet(req2, null);
		assertNotNull(resp2);
		
		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).
				addNested("/vpTest/vtTest/vsTestSearch/testgrid").addAction(Action._get).getMock();
		Object resp1 = controller.handleGet(req1, null);
	}
	
	private void setSession(String user) {
		ClientUser cu = new ClientUser();
		cu.setLoginId(user);
		sessionProvider.setLoggedInUser(cu);
	}
}


