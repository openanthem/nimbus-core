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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorDeleteTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Test
	public void t1_colElem_add() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// create new collection
		MockHttpServletRequest colNew_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._new).getMock();
		
		SampleCoreNestedEntity colElemState_0 = new SampleCoreNestedEntity();
		colElemState_0.setNested_attr_String("0. TEST_INTG_COL_ELEM_add "+ new Date());
		
		SampleCoreNestedEntity colElemState_1 = new SampleCoreNestedEntity();
		colElemState_1.setNested_attr_String("1. TEST_INTG_COL_ELEM_add "+ new Date());
		
		List<SampleCoreNestedEntity> colState = new ArrayList<SampleCoreNestedEntity>();
		colState.add(colElemState_0);
		colState.add(colElemState_1);
		
		String jsonPayload = converter.toJson(colState);
		
		Object colNew_Resp = controller.handlePost(colNew_Req, jsonPayload);
		assertNotNull(colNew_Resp);
		
		// db validation - after add new collection
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colState.size(), core.getAttr_list_1_NestedEntity().size());
		assertEquals(colState.get(0).getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());
		assertEquals(colState.get(1).getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(1).getNested_attr_String());
		
		// do delete of '0'the elem so it reduces the size but elem left would have elemId of '1'
		MockHttpServletRequest colElemDelete_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/list_attached_noConversion_NestedEntity/0").addAction(Action._delete).getMock();
		
		Object colDelete_Resp = controller.handleDelete(colElemDelete_Req, null);
		assertNotNull(colDelete_Resp);
		
		// db validation - after delete
		SampleCoreEntity coreAfter = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colState.size()-1, coreAfter.getAttr_list_1_NestedEntity().size());
		// validate that the earlier "index[1]" elem is now available as "index[0]"
		assertEquals(colState.get(1).getNested_attr_String(), coreAfter.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());


		// object validation - after delete
		MockHttpServletRequest colGet_Req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_1_NestedEntity").addAction(Action._get).getMock();
		
		Object colGet_Resp = controller.handleGet(colGet_Req, null);
		ListParam<SampleCoreNestedEntity> pListNested = ExtractResponseOutputUtils.extractOutput(colGet_Resp);
		assertNotNull(pListNested);
		
		assertEquals(colState.size()-1, pListNested.size());
		assertEquals(colState.get(1).getNested_attr_String(), pListNested.findParamByPath("/1/nested_attr_String").getState());

	}

	@Test
	public void testMappedRootDBDeletion() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Validate the entity was persisted in the before step.
		Assert.assertNotNull(mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS));
		
		// Delete the state for sample_core:1
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Validate the state is null after deletion for sample_core:1
		Assert.assertNull(mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS));
	}
	
	// TODO Investigate why this is failing
	@Ignore
	@Test
	public void testMappedRootDeleteAndRetrieval() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Fetch and validate the state is set for sample_core:1
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addAction(Action._get).getMock();
		
		Object resp_get1 = controller.handleGet(req_get, null);
		Param<Object> p1 = ExtractResponseOutputUtils.extractOutput(resp_get1);
		Assert.assertNotNull(p1.getState());
		
		// Delete the state for sample_core:1
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Fetch and validate the state is null after deletion for sample_core:1
		Object resp_get2 = controller.handleGet(req_get, null);
		Param<Object> p2 = ExtractResponseOutputUtils.extractOutput(resp_get2);
		Assert.assertNull(p2.getState());
	}
	
	@Test
	public void testCoreRootDBDeletion() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Fetch and validate the state is set for sample_core:1
		Assert.assertNotNull(mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS));
		
		// Delete the state for sample_core:1
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Fetch and validate the state is null after deletion for sample_core:1
		Assert.assertNull(mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS));
	}
	
	// TODO Investigate why this is failing
	@Ignore
	@Test
	public void testCollectionDBDeletion() throws JsonProcessingException {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Set and verify the state of a collection element
		MockHttpServletRequest req_set = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._replace).getMock();
		List<String> state = Arrays.asList(new String[] { "one", "two" });
		controller.handlePost(req_set, om.writeValueAsString(state));
		Assert.assertEquals(state, mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS).getAttr_list_String());
		
		// Delete the collection element
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Fetch and validate the state is null after deletion for sample_core:1
		Assert.assertNull(mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS).getAttr_list_String());
	}
	
	// TODO Investigate why this is failing
	@Ignore
	@Test
	public void testCollectionDelete() throws JsonProcessingException {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Set the state of a collection element
		MockHttpServletRequest req_set = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._replace).getMock();
		List<String> state = Arrays.asList(new String[] { "one", "two" });
		controller.handlePost(req_set, om.writeValueAsString(state));
		
		// Verify the state of a collection element was set
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._get).getMock();
		Object resp_get1 = controller.handleGet(req_get, null);
		Param<List<String>> p1 = ExtractResponseOutputUtils.extractOutput(resp_get1);
		Assert.assertEquals(state, p1.getState());
		
		// Delete the collection element
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Fetch and validate the state is null after deletion for sample_core:1
		Object resp_get2 = controller.handleGet(req_get, null);
		Param<List<String>> p2 = ExtractResponseOutputUtils.extractOutput(resp_get2);
		Assert.assertNull(p2.getState());
	}
	
	@Test
	public void testParamDelete() throws JsonProcessingException {
		Long refId = createOrGetDomainRoot_RefId();
		
		// Set the state of a collection element
		MockHttpServletRequest req_set = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/q2").addAction(Action._replace).getMock();
		String state = "one";
		controller.handlePost(req_set, om.writeValueAsString(state));
		
		// Verify the state of a collection element was set
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/q2").addAction(Action._get).getMock();
		Object resp_get1 = controller.handleGet(req_get, null);
		Param<String> p1 = ExtractResponseOutputUtils.extractOutput(resp_get1);
		Assert.assertEquals(state, p1.getState());
		
		// Delete the collection element
		MockHttpServletRequest req_delete = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/q2").addAction(Action._delete).getMock();
		controller.handleGet(req_delete, null);
		
		// Fetch and validate the state is null after deletion for sample_core:1
		Object resp_get2 = controller.handleGet(req_get, null);
		Param<String> p2 = ExtractResponseOutputUtils.extractOutput(resp_get2);
		Assert.assertNull(p2.getState());
	}
}
