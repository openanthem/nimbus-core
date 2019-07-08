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
package com.antheminc.oss.nimbus.channel.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.AbstractFrameworkTest;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;


/**
 * @author Soham Chakravarti
 * @author Tony Lopez
 *
 */
public class WebCommandBuilderTest extends AbstractFrameworkTest {
	
	private WebCommandBuilder testee;
	
	@Before
	public void init() {
		this.testee = new WebCommandBuilder();
	}
	
	@Test
	public void testEvent() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.POST.name(), PLATFORM_ROOT + "/event/notify");
		ModelEvent<String> event = new ModelEvent<>();
		event.setId("/flow_um-case/pg1/caseInfo/requestType");
		event.setType(Action._update.name());
		event.setPayload("oop");
		
		Command cmd = testee.build(httpReq, event);
		assertNotNull(cmd);
		assertSame(Action._update, cmd.getAction());
		assertEquals(CLIENT_ID, cmd.getRootClientAlias());
		assertEquals("flow_um-case", cmd.getRootDomainAlias());
		assertEquals("/flow_um-case/pg1/caseInfo/requestType", cmd.getAbsoluteDomainAlias());
		assertEquals(PLATFORM_ROOT + "/flow_um-case/pg1/caseInfo/requestType/_update", cmd.getAbsoluteUri());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertSame(Behavior.$execute, cmd.getBehaviors().get(0));
	}
	
	@Test
	public void testUriParser() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/member/addr/_search");
		httpReq.addParameter("b", Behavior.$config.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._search, cmd.getAction());
		assertEquals(CLIENT_ID, cmd.getRootClientAlias());
		assertEquals("member", cmd.getRootDomainAlias());
		assertEquals("/member/addr", cmd.getAbsoluteDomainAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
	}

	@Test
	public void testDomainRootOnly1() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_umcase/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals(CLIENT_ID, cmd.getRootClientAlias());
		assertEquals("flow_umcase", cmd.getRootDomainAlias());
		assertEquals("/flow_umcase", cmd.getAbsoluteDomainAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertTrue(cmd.isRootDomainOnly());
	}

	@Test
	public void testDomainRootOnly2() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_umcase:123/pg1/caseInfo/requestType/_update");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._update, cmd.getAction());
		assertEquals(CLIENT_ID, cmd.getRootClientAlias());
		assertEquals("flow_umcase", cmd.getRootDomainAlias());
		assertEquals(Long.valueOf("123"), cmd.getRootDomainElement().getRefId());
		assertEquals("/flow_umcase/pg1/caseInfo/requestType", cmd.getAbsoluteDomainAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertFalse(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testProcessAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_umcase:123/_findPatient:10/_process");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._process, cmd.getAction());
		assertEquals("/flow_umcase/_findPatient", cmd.getAbsoluteDomainAlias());
		assertEquals("/_findPatient", cmd.getProcessAlias());
		assertEquals(Long.valueOf("10"), cmd.getRefId(Type.ProcessAlias));
		assertFalse(cmd.isRootDomainOnly());
	}


	@Test
	public void testUserRoleManagementAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_userrole/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_userrole", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testUserManagementAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testBuildViaRequestOnly() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testContextPathExclusion() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/context-path" + PLATFORM_ROOT + "/flow_client-user/_new");
		httpReq.setContextPath("/context-path");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals(CLIENT_ID,cmd.getRootClientAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testContextPathExclusion2() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/context-path/path2" + PLATFORM_ROOT + "/flow_client-user/_new");
		httpReq.setContextPath("/context-path/path2");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals(CLIENT_ID,cmd.getRootClientAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testEmptyContextPath() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), PLATFORM_ROOT + "/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = testee.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals(CLIENT_ID,cmd.getRootClientAlias());
		assertEquals(APP_ID, cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
}
