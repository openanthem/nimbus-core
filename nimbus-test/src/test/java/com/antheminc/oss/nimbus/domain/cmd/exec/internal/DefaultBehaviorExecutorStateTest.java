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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen.TileGreen;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultBehaviorExecutorStateTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	public void t00_action_new() {
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).addBehavior(Behavior.$state).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Object actual = ExtractResponseOutputUtils.extractOutput(home_newResp);
		assertNotNull(actual);
		assertTrue(VRSampleViewRootEntity.class.isInstance(actual));
	}
	
	@Test
	public void t01_action_get() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest colElemAdd_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._update).getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.toJson(colElemState);
		
		Object colElemAdd_Resp = controller.handlePut(colElemAdd_Req, null, jsonPayload);
		assertNotNull(colElemAdd_Resp);
		
		MockHttpServletRequest get_nested = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile").addAction(Action._get).addBehavior(Behavior.$state).getMock();
		
		Object actualNested = ExtractResponseOutputUtils.extractOutput(controller.handleGet(get_nested, null));
		assertNotNull(actualNested);
		assertTrue(TileGreen.class.isInstance(actualNested));
	}
	
	@Test
	public void t02_action_process() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addAction(Action._process).addBehavior(Behavior.$state).getMock();
		
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Object actual = ExtractResponseOutputUtils.extractOutput(home_newResp);
		assertNotNull(actual);
		assertTrue(VRSampleViewRootEntity.class.isInstance(actual));
	}
	
	//@Config(url = "/p/<!/.m/type!>view/_new?fn=_initEntity&target=/.m/type&json=\"<!/.m/type!>\"&target=/.m/status&json=\"In Progress\"&target=/.m/caseId&json=<!#refId!>&target=/.m/name&json=\"<!/.m/name!>\"")
	@Test
	public void t03_action_new_initEntity() {
		final String K_VAL = "testing "+new Date();
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new)
				.addParam("fn", "_initEntity")
					.addParam("target", "/page_green/tile/level1/audit_nested_attr")
					.addParam("json", "\""+K_VAL+"\"")
				.addParam("b", "$executeAnd$state")
				.getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		Object actual = ParamUtils.extractResponseByClass(home_newResp, VRSampleViewRootEntity.class);
		assertNotNull(actual);
		assertTrue(VRSampleViewRootEntity.class.isInstance(actual));
		
		assertEquals(K_VAL, VRSampleViewRootEntity.class.cast(actual).getPage_green().getTile().getLevel1().getAudit_nested_attr());
	}
}
