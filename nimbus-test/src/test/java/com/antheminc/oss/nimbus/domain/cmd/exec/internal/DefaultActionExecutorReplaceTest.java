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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.drools.core.util.ArrayUtils;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen.ConvertedNestedEntity;

/**
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorReplaceTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Test
	public void t01_col_set() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest colElemAdd_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/section_grid/grid_attached_ConvertedItems").addAction(Action._replace).getMock();
		
		List<ConvertedNestedEntity> colState = new ArrayList<>();
		ConvertedNestedEntity colElemState_1 = new ConvertedNestedEntity();
		colElemState_1.setNested_attr_String("TEST_INTG_COL_ELEM_add_1 "+ new Date());
		
		ConvertedNestedEntity colElemState_2 = new ConvertedNestedEntity();
		colElemState_2.setNested_attr_String("TEST_INTG_COL_ELEM_add_2 "+ new Date());
		colState.add(colElemState_1);
		colState.add(colElemState_2);
		String jsonPayload = converter.toJson(colState);
		
		Object colElemAdd_Resp = controller.handlePost(colElemAdd_Req, jsonPayload);
		assertNotNull(colElemAdd_Resp);
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(2,core.getAttr_list_2_NestedEntity().size());
		assertEquals(colElemState_2.getNested_attr_String(),core.getAttr_list_2_NestedEntity().get(1).getNested_attr_String());
		assertEquals(colElemState_1.getNested_attr_String(), core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
	}
	
	@Test
	public void t02_col_set_existing_core() {
		t02_col_set_existing_internal();
		
		// repeat
		t02_col_set_existing_internal();
	}
	public void t02_col_set_existing_internal() {
		Long refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_replace = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
					.addNested("/attr_list_String").addAction(Action._replace).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		final List<String> listString = new ArrayList<>(Arrays.asList(K_string_arr));
		
		Object resp_arr_update = controller.handlePut(req_arr_replace, null, converter.toJson(listString));
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_String").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<List<String>> cp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(cp_string_arr);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, cp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, core.getAttr_list_String()));
		
	}
	
	@Test
	public void t02_col_set_existing_core_noConversion() {
		Long refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_replace = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
					.addNested("/level1/attr_list_String_noConversion").addAction(Action._replace).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		final List<String> listString = new ArrayList<>(Arrays.asList(K_string_arr));
		
		Object resp_arr_update = controller.handlePut(req_arr_replace, null, converter.toJson(listString));
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/level1/attr_list_String_noConversion").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<List<String>> cp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(cp_string_arr);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, cp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, core.getLevel1().getAttr_list_String_noConversion()));
	}
	
	@Ignore
	@Test
	public void t02_col_set_existing_view_noConversion() {
		t02_col_set_existing_view_noConversion_internal();
		
		// repeat
		t02_col_set_existing_view_noConversion_internal();
	}
	
	public void t02_col_set_existing_view_noConversion_internal() {
		Long refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/attr_list_String_noConversion").addAction(Action._replace).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		final List<String> listString = new ArrayList<>(Arrays.asList(K_string_arr));
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.toJson(listString));
		
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/attr_list_String_noConversion").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<List<String>> vp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(vp_string_arr);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, vp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(CollectionUtils.isEqualCollection(listString, core.getLevel1().getAttr_list_String_noConversion()));
	}
	
	@Test
	public void t03_array_update_core() {
		Long refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/level1/level2b/string_array_b").addAction(Action._replace).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.toJson(K_string_arr));
		
		
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
		Long refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/level2b/string_array_b").addAction(Action._replace).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.toJson(K_string_arr));
		
		
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
}
