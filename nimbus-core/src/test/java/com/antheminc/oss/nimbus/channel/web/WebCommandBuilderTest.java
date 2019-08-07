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

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;

/**
 * @author Soham Chakravarti
 *
 */
public class WebCommandBuilderTest {
	
	static WebCommandBuilder cmdBuilder;

	@BeforeClass
	public static void _setup() {
		cmdBuilder = new WebCommandBuilder();
	}
	
	@Test
	public void t_event() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.POST.name(), "/anthem/icr/p/event/notify");
		ModelEvent<String> event = new ModelEvent<>();
		event.setId("/flow_um-case/pg1/caseInfo/requestType");
		event.setType(Action._update.name());
		event.setPayload("oop");
		
		Command cmd = cmdBuilder.build(httpReq, event);
		assertNotNull(cmd);
		assertSame(Action._update, cmd.getAction());
		assertEquals("anthem", cmd.getRootClientAlias());
		assertEquals("flow_um-case", cmd.getRootDomainAlias());
		assertEquals("/flow_um-case/pg1/caseInfo/requestType", cmd.getAbsoluteDomainAlias());
		assertEquals("/anthem/icr/p/flow_um-case/pg1/caseInfo/requestType/_update", cmd.getAbsoluteUri());
		assertEquals("icr", cmd.getAppAlias());
		assertSame(Behavior.$execute, cmd.getBehaviors().get(0));
	}
	
	@Test
	public void testUriParser() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/anthem/12/icr/p/member/addr/_search");
		httpReq.addParameter("b", Behavior.$config.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._search, cmd.getAction());
		assertEquals("anthem", cmd.getRootClientAlias());
		assertEquals("member", cmd.getRootDomainAlias());
		assertEquals("/member/addr", cmd.getAbsoluteDomainAlias());
		assertEquals("icr", cmd.getAppAlias());
	}

	@Test
	public void t_domainRootOnly_T() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/anthem/icr/p/flow_umcase/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("anthem", cmd.getRootClientAlias());
		assertEquals("flow_umcase", cmd.getRootDomainAlias());
		assertEquals("/flow_umcase", cmd.getAbsoluteDomainAlias());
		assertEquals("icr", cmd.getAppAlias());
		assertTrue(cmd.isRootDomainOnly());
	}

	@Test
	public void t_domainRootOnly_F() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/anthem/icr/p/flow_umcase:123/pg1/caseInfo/requestType/_update");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._update, cmd.getAction());
		assertEquals("anthem", cmd.getRootClientAlias());
		assertEquals("flow_umcase", cmd.getRootDomainAlias());
		assertEquals(Long.valueOf("123"), cmd.getRootDomainElement().getRefId().getId());
		assertEquals("/flow_umcase/pg1/caseInfo/requestType", cmd.getAbsoluteDomainAlias());
		assertEquals("icr", cmd.getAppAlias());
		assertFalse(cmd.isRootDomainOnly());
	}
	
	@Test
	public void t_processAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/anthem/icr/p/flow_umcase:123/_findPatient:10/_process");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._process, cmd.getAction());
		assertEquals("/flow_umcase/_findPatient", cmd.getAbsoluteDomainAlias());
		assertEquals("/_findPatient", cmd.getProcessAlias());
		assertEquals(Long.valueOf("10"), cmd.getRefId(Type.ProcessAlias).getId());
		assertFalse(cmd.isRootDomainOnly());
	}


	@Test
	public void t_userRoleManagementAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/platform/admin/p/flow_userrole/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_userrole", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void t_userManagementAlias() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/platform/admin/p/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq, null);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testBuildViaRequestOnly() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/platform/admin/p/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testContextPathExclusion() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/context-path/client/org/app/p/flow_client-user/_new");
		httpReq.setContextPath("/context-path");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("client",cmd.getRootClientAlias());
		assertEquals("app", cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testContextPathExclusion_2() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/context-path/path2/client/org/app/p/flow_client-user/_new");
		httpReq.setContextPath("/context-path/path2");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("client",cmd.getRootClientAlias());
		assertEquals("app", cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
	
	@Test
	public void testEmptyContextPath() {
		MockHttpServletRequest httpReq = new MockHttpServletRequest(HttpMethod.GET.name(), "/client/org/app/p/flow_client-user/_new");
		httpReq.addParameter("b", Behavior.$execute.name());
		
		Command cmd = cmdBuilder.build(httpReq);
		assertNotNull(cmd);
		assertSame(Action._new, cmd.getAction());
		assertEquals("client",cmd.getRootClientAlias());
		assertEquals("app", cmd.getAppAlias());
		assertEquals("/flow_client-user", cmd.getAbsoluteDomainAlias());
		assertTrue(cmd.isRootDomainOnly());
	}
}
