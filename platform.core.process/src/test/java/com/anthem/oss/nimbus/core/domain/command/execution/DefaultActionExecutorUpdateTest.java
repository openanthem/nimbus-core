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
		MockHttpServletRequest org_getReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._update).getMock();
		
		SampleCoreNestedEntity colElemEntityState = new SampleCoreNestedEntity();
		colElemEntityState.setNested_attr_String("TEST_INTG_COL_ELEM_add "+ new Date());
		String jsonPayload = converter.convert(colElemEntityState);
		
		Object org_getResp = controller.handlePut(org_getReq, null, jsonPayload);
		assertNotNull(org_getResp);
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colElemEntityState.getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());
	}
}
