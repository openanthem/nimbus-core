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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.LockEntity;
/**
 * @author Sandeep Mantha
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PessimisticLockTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	SessionProvider sessionProvider;

	protected static final String LOCK_VIEW_DOMAIN = PLATFORM_ROOT + "/lockableviewdomain";

	protected static final String ROOT_DOMAIN_WITH_MAPSTO = PLATFORM_ROOT + "/rootdomain";

	protected static final String SAMPLE_LANDING_VIEW = PLATFORM_ROOT + "/samplelandingview";

	protected static final String VIEW_LOCKALIAS_LANDING = PLATFORM_ROOT + "/viewdomainwithmapstolockalias";

	protected static final String ROOT_LANDING = PLATFORM_ROOT + "/rootlandingview";

	// 1. _new on view 2. _update on view 3. mock another session and _get on refId
	@Test
	public void t01_domainLock() {
		setSession("user1");

		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);

		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		try {
			sessionProvider.clear();
			setSession("user2");
			MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId).addAction(Action._get).getMock();
			Object resp2 = controller.handleGet(req2, null);
		} catch(FrameworkRuntimeException e) {
			assertEquals(e.getExecuteError().getMessage(), "Domain entity: samplelockabledomain locked with refId: 1");
		}
	}

	//1. _new on view 2. _update on view 3. _nav on view
	@Test
	public void t02_domainLock() {
		setSession("user1");

		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);

		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		MockHttpServletRequest nav_req = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId).addAction(Action._nav).
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
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);

		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		MockHttpServletRequest home_newReq1 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).addAction(Action._new).getMock();
		Object home_resp1 = controller.handleGet(home_newReq1, null);
		assertNotNull(home_resp1);

		MockHttpServletRequest new_req1 = MockHttpRequestBuilder.withUri(SAMPLE_LANDING_VIEW).
				addNested("/vpTest/vtTest/vsTestSearch/testgrid").addAction(Action._get).getMock();
		Object new_resp1 = controller.handleGet(new_req1, null);

		sessionProvider.clear();
		setSession("user1");
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


	//1. _new on landing view with no mapsTo 2. _new on domain with mapsTo 3. _update on domain 4. mock ui redirection by _get to landing view followed by event to reset domains from UI
	@Test
	public void t04_domainLock() {

		setSession("user1");
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_LANDING).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);

		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addAction(Action._new).getMock();
		Object resp1 = controller.handleGet(req1, null);
		assertNotNull(resp1);

		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(resp1);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_DOMAIN_WITH_MAPSTO).addRefId(refId)
				.addNested("/attr1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(ROOT_LANDING).addAction(Action._get).getMock();
		Object resp2 = controller.handleGet(req2, null);
		assertNotNull(home_newResp);

		List<LockEntity> lst = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst.size(), 1);

		MockHttpServletRequest req3 = MockHttpRequestBuilder.withUri(ROOT_LANDING).addNested("/vpTest").addAction(Action._get).getMock();
		Object resp3 = controller.handleGet(req3, null);
		assertNotNull(resp3);

		List<LockEntity> lst2 = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst2.size(), 0);
	}

	@Test
	public void t05_domainwithLockalias() {
		setSession("user1");

		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_LANDING).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		MockHttpServletRequest home_newReq1 = MockHttpRequestBuilder.withUri(ROOT_LANDING)
				.addNested("/vpTest/vtTest/test").addAction(Action._update).getMock();
		Object home_newRes1 = controller.handlePut(home_newReq1, null, converter.toJson("test 1"));
				
		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(VIEW_LOCKALIAS_LANDING).addAction(Action._new).getMock();
		Object resp1 = controller.handleGet(req1, null);
		assertNotNull(resp1);

		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(resp1);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_LOCKALIAS_LANDING).addRefId(refId)
				.addNested("/p1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));

		List<LockEntity> lst = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst.size(), 1);
		assertEquals(lst.get(0).getAlias(), "sublock");

		MockHttpServletRequest req3 = MockHttpRequestBuilder.withUri(ROOT_LANDING).addNested("/vpTest/vtTest/test").addAction(Action._get).getMock();
		Object resp3 = controller.handleGet(req3, null);
		EntityState.Param<?> viewParam = ExtractResponseOutputUtils.extractOutput(resp3);
		assertEquals(viewParam.getState(),"lockacquired");
	}
	
	@Test @Ignore
	public void t06_viewdomainlock() {
		setSession("user1");

		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(ROOT_LANDING).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(LOCK_VIEW_DOMAIN+"/_new?fn=_initEntity&target=/id&json=\"1\"").getMock();
		Object resp1 = controller.handleGet(req1, null);
		assertNotNull(resp1);
		
		Long refId  = ExtractResponseOutputUtils.extractDomainRootRefId(resp1);

		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(LOCK_VIEW_DOMAIN)
				.addNested("/p1").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test 2"));
	
		List<LockEntity> lst = mongo.findAll(LockEntity.class, "lock");
		assertEquals(lst.size(), 1);
	}
	
	private void setSession(String user) {
		ClientUser cu = new ClientUser();
		cu.setLoginId(user);
		sessionProvider.setLoggedInUser(cu);
	}
}