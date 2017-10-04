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
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.ui.VPSampleViewPageGreen.ConvertedNestedEntity;

import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorReplaceTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Test
	public void t1_col_set() {
		String refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest colElemAdd_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/section_grid/grid_attached_ConvertedItems").addAction(Action._replace).getMock();
		
		List<ConvertedNestedEntity> colState = new ArrayList<>();
		ConvertedNestedEntity colElemState_1 = new ConvertedNestedEntity();
		colElemState_1.setNested_attr_String("TEST_INTG_COL_ELEM_add_1 "+ new Date());
		
		ConvertedNestedEntity colElemState_2 = new ConvertedNestedEntity();
		colElemState_2.setNested_attr_String("TEST_INTG_COL_ELEM_add_2 "+ new Date());
		colState.add(colElemState_1);
		colState.add(colElemState_2);
		String jsonPayload = converter.convert(colState);
		
		Object colElemAdd_Resp = controller.handlePost(colElemAdd_Req, jsonPayload);
		assertNotNull(colElemAdd_Resp);
		
		// db validation
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(2,core.getAttr_list_2_NestedEntity().size());
		assertEquals(colElemState_2.getNested_attr_String(),core.getAttr_list_2_NestedEntity().get(1).getNested_attr_String());
		assertEquals(colElemState_1.getNested_attr_String(), core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
	}
}
