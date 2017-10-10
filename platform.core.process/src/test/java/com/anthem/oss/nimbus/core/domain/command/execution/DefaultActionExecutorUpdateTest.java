/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

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

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreNestedEntity;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

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
		String jsonPayload = converter.convert(colElemState);
		
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
		
		String jsonPayload = converter.convert(colElemState);
		
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
				.addNested("/level1/level2/string_array").addAction(Action._update).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.convert(K_string_arr));
		
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/level1/level2/string_array").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<String[]> cp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(cp_string_arr);
		
		assertTrue(ArrayUtils.equals(K_string_arr, cp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(ArrayUtils.equals(K_string_arr, core.getLevel1().getLevel2().getString_array()));
	}
	
	@Test
	public void t04_array_update_view() {
		String refId = createOrGetDomainRoot_RefId();
		
		MockHttpServletRequest req_arr_update = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/level2/string_array").addAction(Action._update).getMock();
		
		final String[] K_string_arr = new String[]{"A", "B", "C @ "+new Date()};
		Object resp_arr_update = controller.handlePut(req_arr_update, null, converter.convert(K_string_arr));
		
		
		// validate via Param
		MockHttpServletRequest req_arr_get = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/level1/level2/string_array").addAction(Action._get).getMock();
		
		Object resp_arr_get = controller.handleGet(req_arr_get, null);
		EntityState.Param<String[]> vp_string_arr = ExtractResponseOutputUtils.extractOutput(resp_arr_get);
		assertNotNull(vp_string_arr);
		
		assertTrue(ArrayUtils.equals(K_string_arr, vp_string_arr.getState()));
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
		assertTrue(ArrayUtils.equals(K_string_arr, core.getLevel1().getLevel2().getString_array()));
	}
}
