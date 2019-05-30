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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.fn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageBlue.Section_ConvertedNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen.ConvertedNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageRed.Form_ConvertedNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultParamFunctionHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	public void t01_get_core() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
					.addAction(Action._get)
					.addParam(Constants.KEY_FUNCTION.code, "param")
					.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "getState()")
					.getMock();
		
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		SampleCoreEntity core = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(core);
		
		assertEquals(refId, core.getId());
	}
	

	@Test
	public void t02_get_view_mapsTo() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addAction(Action._get)
					.addParam(Constants.KEY_FUNCTION.code, "param")
					.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "getMapsTo().getState()")
					.getMock();
		
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		SampleCoreEntity core = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(core);
		
		assertEquals(refId, core.getId());
	}
	
	@Test
	public void t03_arg_findParamByPath() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addAction(Action._get)
					.addParam(Constants.KEY_FUNCTION.code, "param")
					.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "findParamByPath('/page_green')")
					.getMock();
		
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		Param<VPSampleViewPageGreen> pPage_green = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(pPage_green);
		assertTrue(pPage_green.getPath().endsWith("page_green"));
	}
	
	@Test
	public void t04_arg_getElem_index() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// add collection in core
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_1_NestedEntity")
				.addAction(Action._update)
				.getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.toJson(colElemState);
		
		Object updateResp = controller.handlePut(updateReq, null, jsonPayload);
		assertNotNull(updateResp);
		
		// get fn=param
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/list_attached_noConversion_NestedEntity")
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "getLeafState(0)")
				.getMock();
		
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		SampleCoreNestedEntity output = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(output);
		assertEquals(colElemState.getNested_attr_String(), output.getNested_attr_String());
	}
	
	@Test
	public void t05_assign_state_set_add_new() {
		Long refId = createOrGetDomainRoot_RefId();

		// assign for add
		MockHttpServletRequest fnAssignReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "assignMapsTo(getRootDomain().findParamByPath('/page_red/.m/attr_list_2_NestedEntity'))")
				.getMock();
		
		Object fnAssignResp = controller.handleGet(fnAssignReq, null);
		assertNotNull(fnAssignResp);
		

		// create new view element instance: simulate form submission
		final String K_VAL = "setting from form at: "+ new Date();
		
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL);
		String jsonPayload = converter.toJson(form);
		
		// user clicks on save button :: set to form 
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/saveButton")
				.addAction(Action._get)
				.getMock();
		
		Object updateResp = controller.handlePut(updateReq, null, jsonPayload);
		assertNotNull(updateResp);
		
		// verify if element got added to core mapped collection
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "getState()")
				.getMock();
	
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		SampleCoreEntity core = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(core);
		
		assertEquals(1, core.getAttr_list_2_NestedEntity().size());
		assertEquals(K_VAL, core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
	}
	
	@Test
	public void t06_assign_by_addButton() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// assign for add
		MockHttpServletRequest fnAssignReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/addButton")
				.addAction(Action._get)
				.getMock();
		
		Object fnAssignResp = controller.handleGet(fnAssignReq, null);
		assertNotNull(fnAssignResp);
		
		// add value to mapsTo core to see effect in mapped transient
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/.m")
				.addAction(Action._update)
				.getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.toJson(colElemState);
		
		Object updateResp = controller.handlePut(updateReq, null, jsonPayload);
		assertNotNull(updateResp);
		
		// call flush to simulate save call
		MockHttpServletRequest fnReq_flush = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/")
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "flush()")
				.getMock();
		controller.handleGet(fnReq_flush, null);
		
		// get fn=param
		MockHttpServletRequest fnReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/section_grid/grid_attached_ConvertedItems")
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code, "getLeafState(0)")
				.getMock();
		
		Object fnResp = controller.handleGet(fnReq, null);
		assertNotNull(fnResp);
		
		ConvertedNestedEntity output = ExtractResponseOutputUtils.extractOutput(fnResp);
		assertNotNull(output);
		assertEquals(colElemState.getNested_attr_String(), output.getNested_attr_String());
	}
	
	@Test
	public void t07_assign_addForm_addCore_assignEdit_updateExisting() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// user clicks add button :: assign for add 
		MockHttpServletRequest fnAssignReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/addButton")
				.addAction(Action._get)
				.getMock();
		
		Object fnAssignResp = controller.handleGet(fnAssignReq, null);
		assertNotNull(fnAssignResp);
		
		// user submit form data 
		final String K_VAL_0 = "setting from form at: "+ new Date();
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL_0);
		String jsonFormPayload = converter.toJson(form);
		
		MockHttpServletRequest submitFormReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/saveButton")
				.addAction(Action._get)
				.getMock();
		
		Object submitFormResp = controller.handlePut(submitFormReq, null, jsonFormPayload);
		assertNotNull(submitFormResp);
		
		// add value to mapsTo core to see effect in mapped transient
		MockHttpServletRequest updateCoreReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_2_NestedEntity")
				.addAction(Action._update)
				.getMock();
		
		final String K_VAL_1 = "TEST_INTG_COL_ELEM_add "+ new Date();
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String(K_VAL_1);
		String jsonPayload = converter.toJson(colElemState);
		
		Object updateCoreResp = controller.handlePut(updateCoreReq, null, jsonPayload);
		assertNotNull(updateCoreResp);
		
		// validate mapped view grid is updated with 2 records
		MockHttpServletRequest fnGetConvertedGridListReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/vm_attached_convertedList")
				.addAction(Action._get)
				.getMock();
		
		Object fnGetConvertedGridListResp = controller.handleGet(fnGetConvertedGridListReq, null);
		assertNotNull(fnGetConvertedGridListResp);
		
		ListParam<Section_ConvertedNestedEntity> listGridConverted = ExtractResponseOutputUtils.extractOutput(fnGetConvertedGridListResp);
		assertNotNull(listGridConverted);
		assertSame(2, listGridConverted.size());
		assertEquals(K_VAL_0, listGridConverted.getLeafState().get(0).getVm_nested_attr_String());
		assertEquals(K_VAL_1, listGridConverted.getLeafState().get(1).getVm_nested_attr_String());
		
		
		// user clicks on edit link in grid :: assign colElem for edit
		MockHttpServletRequest fnAssignEditReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/vm_attached_convertedList/0/editButton")
				.addAction(Action._get)
				.getMock();
		
		Object fnAssignEditResp = controller.handleGet(fnAssignEditReq, null);
		assertNotNull(fnAssignEditResp);
		
		// user submits form to update existing 
		final String K_VAL_0_updated = "updating from form at: "+ new Date();
		form.setVt_nested_attr_String(K_VAL_0_updated);
		String jsonPayload_updated = converter.toJson(form);
		
		MockHttpServletRequest submitUpdateFormReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/saveButton")
				.addAction(Action._get)
				.getMock();
		
		Object submitUpdateFormResp = controller.handlePut(submitUpdateFormReq, null, jsonPayload_updated);
		assertNotNull(submitUpdateFormResp);
		
		// validate in db
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(K_VAL_0_updated, core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
		assertEquals(K_VAL_1, core.getAttr_list_2_NestedEntity().get(1).getNested_attr_String());
	}
	
	@Test
	public void t08_assign_existing_delete() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// add value to mapsTo core
		List<ConvertedNestedEntity> nestedCol = new ArrayList<>();
		
		final String K_VAL_0 = "0. TEST_INTG_COL_ELEM_add "+ new Date();
		ConvertedNestedEntity elem0 = new ConvertedNestedEntity();
		elem0.setNested_attr_String(K_VAL_0);
		nestedCol.add(elem0);
		
		final String K_VAL_1 = "1. TEST_INTG_COL_ELEM_add "+ new Date();
		ConvertedNestedEntity elem1 = new ConvertedNestedEntity();
		elem1.setNested_attr_String(K_VAL_1);
		nestedCol.add(elem1);
		
		MockHttpServletRequest setColReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/section_grid/grid_attached_ConvertedItems")
				.addAction(Action._new)
				.getMock();
		
		Object setColResp = controller.handlePost(setColReq, converter.toJson(nestedCol));
		assertNotNull(setColResp);
		
		// assign 0th elem to transient parameter -- user clicks on edit link in grid :: assign colElem for edit
		MockHttpServletRequest fnAssignEditReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/vm_attached_convertedList/0/editButton")
				.addAction(Action._get)
				.getMock();
		
		Object fnAssignEditResp = controller.handleGet(fnAssignEditReq, null);
		assertNotNull(fnAssignEditResp);
		
		// simulate delete action
		MockHttpServletRequest deleteReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity/deleteButton")
				.addAction(Action._get)
				.getMock();
		
		Object deleteResp = controller.handleGet(deleteReq, null);
		assertNotNull(deleteResp);
		
		// validate
		MockHttpServletRequest getColReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/section_grid/grid_attached_ConvertedItems")
				.addAction(Action._get)
				.getMock();
		
		Object getColResp = controller.handleGet(getColReq, null);
		assertNotNull(getColResp);
		
		ListParam<ConvertedNestedEntity> listGridConverted = ExtractResponseOutputUtils.extractOutput(getColResp);
		assertSame(1, listGridConverted.size());
		assertEquals(K_VAL_1, listGridConverted.findParamByPath("/1/nested_attr_String").getState());
		
		// validate in db
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertSame(1, core.getAttr_list_2_NestedEntity().size());
		assertEquals(K_VAL_1, core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
	}
}
