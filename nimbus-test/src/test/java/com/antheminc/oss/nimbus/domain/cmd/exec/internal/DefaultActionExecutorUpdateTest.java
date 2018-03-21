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

import org.drools.core.util.ArrayUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleNoConversionEntity.NestedNoConversionLevel1;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageRed.Form_ConvertedNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorUpdateTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Test
	public void t01_colElem_add() {
		String refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest colElemAdd_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._update).getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.write(colElemState);
		
		Object colElemAdd_Resp = controller.handlePut(colElemAdd_Req, null, jsonPayload);
		assertNotNull(colElemAdd_Resp);
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colElemState.getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());
	}
	
	@Test
	public void t02_colElem_edit() {
		String refId = createOrGetDomainRoot_RefId();
		
		// create of get entry in db
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		if(core==null || CollectionUtils.isEmpty(core.getAttr_list_1_NestedEntity())) {
			t01_colElem_add();
			
			// get now
			core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
			assertNotNull(core);
		}
		
		// existing 0th elem
		SampleCoreNestedEntity colElemState = core.getAttr_list_1_NestedEntity().get(0);
		assertNotNull(colElemState);
		
		// get ListParam for col
		MockHttpServletRequest colGet_req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._get).getMock();
		
		Object colGet_resp = controller.handleGet(colGet_req, null);
		ListParam<SampleCoreNestedEntity> pListNested = ExtractResponseOutputUtils.extractOutput(colGet_resp);
		assertNotNull(pListNested);
		
		// update
		String updatedVal = colElemState.getNested_attr_String() + " - updated "+new Date();
		colElemState.setNested_attr_String(updatedVal);
		
		String jsonPayload = converter.write(colElemState);
		
		String elemId = pListNested.getType().getModel().getParams().get(0).findIfCollectionElem().getElemId();
		
		MockHttpServletRequest colElemUpdate_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/list_attached_noConversion_NestedEntity/"+elemId).addAction(Action._update).getMock();
		
		Object colElemUpdate_Resp = controller.handlePut(colElemUpdate_Req, null, jsonPayload);
		assertNotNull(colElemUpdate_Resp);
		
		// db validation
		core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colElemState.getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());
	}
	
	@Test
	public void t03_array_update_core() {
		String refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/level1/level2b/string_array_b").addAction(Action._update).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.write(K_string_arr));
		
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/level1/level2b/string_array_b").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<String[]> cp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(cp_string_arr);
		
		assertTrue(ArrayUtils.equals(K_string_arr, cp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(ArrayUtils.equals(K_string_arr, core.getLevel1().getLevel2b().getString_array_b()));
	}
	
	@Test
	public void t04_array_update_view() {
		String refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/level2b/string_array_b").addAction(Action._update).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.write(K_string_arr));
		
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/level2b/string_array_b").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<String[]> vp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(vp_string_arr);
		
		assertTrue(ArrayUtils.equals(K_string_arr, vp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(ArrayUtils.equals(K_string_arr, core.getLevel1().getLevel2b().getString_array_b()));
	}
	
	@Test
	public void t05_updateOnlyGivenFields() {
		String refId = createOrGetDomainRoot_RefId();
		
		// Build the request for updating the form
		MockHttpServletRequest req_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/view_nc_form/nc_nested0_Details/nc_nested_level1")
				.addAction(Action._update)
				.getMock();
		
		// Build the request for retrieving the form
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/view_nc_form/nc_nested0_Details/nc_nested_level1")
				.addAction(Action._get)
				.getMock();
		
		// Set an initial value to one field
		String payload = "{ \"nested_nc_attr1A\":\"initially_set_value\" }";
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		Object resp_get = controller.handleGet(req_get, null);
		EntityState.Param<NestedNoConversionLevel1> viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		assertEquals("initially_set_value", viewParam.getLeafState().getNested_nc_attr1A());
		
		// Set a second value to a different field
		payload = "{ \"nested_nc_attr1B\":\"new_value_from_update\" }";
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		resp_get = controller.handleGet(req_get, null);
		viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		assertEquals("new_value_from_update", viewParam.getLeafState().getNested_nc_attr1B());
		
		// Validate original value still exists
		assertEquals("initially_set_value", viewParam.getLeafState().getNested_nc_attr1A());
	}
	
	@Test
	public void t06_updateOnlyGivenFields_nested() {
		String refId = createOrGetDomainRoot_RefId();
		
		// Build the request for updating the form
		MockHttpServletRequest req_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/view_nc_form/nc_nested0_Details/nc_nested_level1")
				.addAction(Action._update)
				.getMock();
		
		// Build the request for retrieving the form
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/view_nc_form/nc_nested0_Details/nc_nested_level1")
				.addAction(Action._get)
				.getMock();
		
		// Set an initial value to one field
		String payload = 
			"{ " + 
				"\"nested_nc_attr1A\": \"initially_set_value\", " +
				"\"nc_nested_level2\": { " + 
					"\"nested_nc_attr2C\": \"initially_set_value\"" +
				"}" + 
			"}";
		
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		Object resp_get = controller.handleGet(req_get, null);
		EntityState.Param<NestedNoConversionLevel1> viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		assertEquals("initially_set_value", viewParam.getLeafState().getNested_nc_attr1A());
		assertEquals("initially_set_value", viewParam.getLeafState().getNc_nested_level2().getNested_nc_attr2C());
		
		// Set a second value to a different field
		payload = "{ \"nested_nc_attr1B\":\"new_value_from_update\" }";
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		resp_get = controller.handleGet(req_get, null);
		viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		assertEquals("new_value_from_update", viewParam.getLeafState().getNested_nc_attr1B());
		
		// Validate original value still exists
		assertEquals("initially_set_value", viewParam.getLeafState().getNested_nc_attr1A());
		assertEquals("initially_set_value", viewParam.getLeafState().getNc_nested_level2().getNested_nc_attr2C());
	}
	
	@Test
	public void t07_updateOnlyGivenFields_transient() {
		String refId = createOrGetDomainRoot_RefId();
		
		// Build the request for updating the form
		MockHttpServletRequest req_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._update)
				.getMock();
		
		// Build the request for assigning maps to
		MockHttpServletRequest req_assignMapsTo = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._get)
				.addParam("fn", "param")
				//.addParam("expr", "assignMapsTo('/.d/.m/attr_NestedEntity')")
				.addParam("expr", "assignMapsTo()")
				.getMock();
		
		// Build the request for retrieving the form
		MockHttpServletRequest req_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._get)
				.getMock();
		
		// Assign the transient param
		this.controller.handleGet(req_assignMapsTo, null);
		
		// Set an initial value to one field
		String payload = 
			"{ " + 
				"\"vt_nested_attr_String\": \"initially_set_value\"" +
			"}";
		
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		Object resp_get = controller.handleGet(req_get, null);
		EntityState.Param<Form_ConvertedNestedEntity> viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		// TODO - This fails with a null value. Why is it not being retrieved?
		assertEquals("initially_set_value", viewParam.getLeafState().getVt_nested_attr_String());
		
		// Set a second value to a different field
		payload = "{ \"vt_nested_attr_String2\":\"new_value_from_update\" }";
		this.controller.handlePut(req_update, null, payload);
		
		// Validate the value was set
		resp_get = controller.handleGet(req_get, null);
		viewParam = ExtractResponseOutputUtils.extractOutput(resp_get);
		assertEquals("new_value_from_update", viewParam.getLeafState().getVt_nested_attr_String2());
		
		// Validate original value still exists
		assertEquals("initially_set_value", viewParam.getLeafState().getVt_nested_attr_String());
	}
}
