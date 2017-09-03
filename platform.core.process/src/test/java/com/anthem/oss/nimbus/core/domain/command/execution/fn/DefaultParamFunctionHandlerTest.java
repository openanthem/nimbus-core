/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreNestedEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageBlue.Section_ConvertedNestedEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageGreen;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageGreen.ConvertedNestedEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageRed.Form_ConvertedNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultParamFunctionHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	public void t01_get_core() {
		String refId = createOrGetDomainRoot_RefId();
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
		String refId = createOrGetDomainRoot_RefId();
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
		String refId = createOrGetDomainRoot_RefId();
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
		String refId = createOrGetDomainRoot_RefId();
		
		// add collection in core
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_1_NestedEntity")
				.addAction(Action._update)
				.getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.convert(colElemState);
		
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
		String refId = createOrGetDomainRoot_RefId();

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
		String jsonPayload = converter.convert(form);
		
		// set to form
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._update)
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
		String refId = createOrGetDomainRoot_RefId();
		
		// assign for add
		MockHttpServletRequest fnAssignReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_blue/tile/addButton")
				.addAction(Action._get)
				.getMock();
		
		Object fnAssignResp = controller.handleGet(fnAssignReq, null);
		assertNotNull(fnAssignResp);
		
		// add value to mapsTo core to see effect in mapped transient
		MockHttpServletRequest updateReq = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_2_NestedEntity")
				.addAction(Action._update)
				.getMock();
		
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.convert(colElemState);
		
		Object updateResp = controller.handlePut(updateReq, null, jsonPayload);
		assertNotNull(updateResp);
		
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
		String refId = createOrGetDomainRoot_RefId();
		
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
		String jsonFormPayload = converter.convert(form);
		
		MockHttpServletRequest submitFormReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._update)
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
		String jsonPayload = converter.convert(colElemState);
		
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
		
		MockHttpServletRequest submitUpdateFormReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._update)
				.getMock();
		
		Object submitUpdateFormResp = controller.handlePut(submitUpdateFormReq, null, jsonFormPayload);
		assertNotNull(submitUpdateFormResp);
		
		// validate in db
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(K_VAL_0_updated, core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
		assertEquals(K_VAL_1, core.getAttr_list_2_NestedEntity().get(1).getNested_attr_String());
	}
}
