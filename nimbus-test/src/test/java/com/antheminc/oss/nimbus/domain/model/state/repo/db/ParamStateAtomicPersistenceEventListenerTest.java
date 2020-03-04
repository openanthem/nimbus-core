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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.CommandUtils;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.antheminc.oss.nimbus.test.scenarios.repo.core.SampleRepoDifferentAlias;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s9.view.VR_SampleMongoAudit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
@EnableAutoConfiguration
public class ParamStateAtomicPersistenceEventListenerTest extends AbstractFrameworkIntegrationTests {

	protected static final String VIEW_PARAM_ROOT = PLATFORM_ROOT + "/sample_s9_view";

	@Autowired
	@Qualifier("default.processGateway")
	private CommandExecutorGateway commandGateway;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private MongoTemplate mongo;

	@Test
	public void testDomainRepoAliasMismatchPersistence() throws JsonProcessingException {
		final String requestUri = "app/org/p/sample_repo_diff_alias/_new";
		final String expectedCollectionName = "person_of_interest";
		SampleRepoDifferentAlias expected = new SampleRepoDifferentAlias("Oscar", "Grouch");

		Command cmd = CommandUtils.prepareCommand(requestUri);
		final String jsonPayload = this.om.writeValueAsString(expected);
		CommandMessage cmdMsg = new CommandMessage(cmd, jsonPayload);
		this.commandGateway.execute(cmdMsg);

		// ensure the persisted entity is matching the value provided for
		// @Repo.alias, not @Domain.value
		Assert.assertEquals(expected, mongo.findById(1L, SampleRepoDifferentAlias.class, expectedCollectionName));
	}
	
	@Test
	public void testAuditView() {
		MockHttpServletRequest newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object newResp = controller.handleGet(newReq, null);
		assertNotNull(newResp);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(newResp);
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(domainRoot_refId)
				.addNested("/vp/vt/vs/vf/attr1").addAction(Action._update).getMock();
		Object response1 = controller.handlePut(request, null, converter.toJson("test1"));
		Param<List<String>> viewParam = ParamUtils.extractResponseByParamPath(response1, "/vp/vt/vs/vf/attr1");
		assertEquals("test1", viewParam.getState());
		
		List<VR_SampleMongoAudit> auditList = mongo.findAll(VR_SampleMongoAudit.class, "sample_s9_auditview");
		assertEquals(auditList.size(), 1);
		assertEquals(auditList.get(0).getAttr1(),"test1");
		assertNull(auditList.get(0).getAttr2());

		
		MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(domainRoot_refId)
				.addNested("/vp/vt/vs/vf/attr2").addAction(Action._update).getMock();
		Object response2 = controller.handlePut(request2, null, converter.toJson("test2"));
		Param<List<String>> viewParam2 = ParamUtils.extractResponseByParamPath(response2, "/vp/vt/vs/vf/attr2");
		assertEquals("test2", viewParam2.getState());
		
		List<VR_SampleMongoAudit> auditLst = mongo.findAll(VR_SampleMongoAudit.class, "sample_s9_auditview");
		assertEquals(auditLst.size(), 2);
		assertEquals(auditLst.get(0).getAttr1(),"test1");
		assertNull(auditLst.get(0).getAttr2());
		assertEquals(auditLst.get(1).getAttr1(),"test1");
		assertEquals(auditLst.get(1).getAttr2(),"test2");

	}
}
