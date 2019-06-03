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
package com.antheminc.oss.nimbus.test.scenarios.s6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s6.core.S6C_CoreMain;
import com.antheminc.oss.nimbus.test.scenarios.s6.core.S6C_CoreMain.S6_CoreNested;

/**
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S6_NestedColDomainUpdateTest extends AbstractFrameworkIntegrationTests {
	
	public static String CORE_ROOT = PLATFORM_ROOT + "/s6c_main";
	
	public static final Long CORE_REF_ID = new Long(1);
	
	@Autowired SessionProvider sessionProvider;
	
	private Object createNew_s6_coremain() {
		
		S6C_CoreMain s6_core = new S6C_CoreMain();
		s6_core.setId(CORE_REF_ID);
		s6_core.setAttr("attr");
		
		List<S6_CoreNested> attr_list_nested = new ArrayList<>();
		S6_CoreNested s6_core_nested_1 = new S6_CoreNested();
		s6_core_nested_1.setAttr_nested("attr_nested");
		attr_list_nested.add(s6_core_nested_1);
		
		s6_core.setAttr_list_s6_nested(attr_list_nested);
		
		String payload = converter.toJson(s6_core);
		
		return controller.handlePost(
					MockHttpRequestBuilder.withUri(CORE_ROOT).addAction(Action._new)
					.addParam("b", "$executeAnd$state").getMock(), 
					payload);
	}
	
	/**
	 * This test case illustrates the scenario of DB update for a _get call on a domain with nested collections
	 * Actual: lastModifiedBy, lastModifiedDate is being updated for a _get call although there is no setState on the domain
	 * Expected: lastModifiedBy, lastModifiedDate should not be updated on a _get call
	 */
	@Test
	public void t00_nested_collection_get_domain_update() {
		Object controllerResp_new = createNew_s6_coremain();

		assertNotNull(controllerResp_new);	
		Object actual = ExtractResponseOutputUtils.extractOutput(controllerResp_new, 1);
		assertNotNull(actual);
		assertTrue(S6C_CoreMain.class.isInstance(actual));
		
		S6C_CoreMain actual_core = (S6C_CoreMain)actual;
		ZonedDateTime actualUpdatedTime = actual_core.getLastModifiedDate();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		sessionProvider.removeAttribute("{/s6c_main:"+CORE_REF_ID+"}");
		
		Object controllerResp_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(CORE_ROOT)
				.addRefId(CORE_REF_ID)
				.addAction(Action._get)
				.addParam("b", "$state").getMock(), 
				null);
		Object s6_core_output = ExtractResponseOutputUtils.extractOutput(controllerResp_get, 0);
		assertTrue(S6C_CoreMain.class.isInstance(s6_core_output));
		
		S6C_CoreMain s6_core = (S6C_CoreMain)s6_core_output;
		ZonedDateTime s6_core_updatedtime = s6_core.getLastModifiedDate();
		
		assertEquals(actual_core.getAttr(),s6_core.getAttr());
		assertEquals(actual_core.getAttr_list_s6_nested().size(),s6_core.getAttr_list_s6_nested().size());
		assertEquals(actual_core.getAttr_list_s6_nested().get(0).getAttr_nested(),s6_core.getAttr_list_s6_nested().get(0).getAttr_nested());
		assertEquals(actualUpdatedTime,s6_core_updatedtime);
	}
}
