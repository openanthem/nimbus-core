/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorUpdateTest extends AbstractFrameworkIngerationTests {
	
	private static final String CORE_DOMAIN_ALIAS = "sample_core";
	
	private static final String CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + CORE_DOMAIN_ALIAS; // /attr_list_1_NestedEntity";
	private static final String VIEW_PARAM_ROOT = PLATFORM_ROOT + "/sample_view"; // /page_green/tile/list_attached_noConversion_NestedEntity";
	
	private static String domainRoot_refId;
	
	public synchronized String createOrGetDomainRoot_RefId() {
		if(domainRoot_refId!=null) 
			return domainRoot_refId;
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		return domainRoot_refId;
	}
	
	@Test
	public void t1_colElem_add() {
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
	public void t2_colElem_edit() {
		String refId = createOrGetDomainRoot_RefId();
		
		// create of get entry in db
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		if(core==null) {
			t1_colElem_add();
			
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
}
