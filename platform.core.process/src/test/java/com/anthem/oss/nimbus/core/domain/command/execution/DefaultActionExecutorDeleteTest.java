/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreNestedEntity;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorDeleteTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Test
	public void t1_colElem_add() {
		String refId = createOrGetDomainRoot_RefId();
		
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
		
		String jsonPayload = converter.convert(colState);
		
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

}
