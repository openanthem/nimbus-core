/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreNestedEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageGreen;

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
}
