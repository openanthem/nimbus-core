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
package com.antheminc.oss.nimbus.domain.model.config.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventHandlerOrderedExecutionTest extends AbstractFrameworkIntegrationTests {

	private static final String K_URI_VR = PLATFORM_ROOT + "/s5c_main";
	
	private Object createNew_VR() {
		return controller.handleGet(
					MockHttpRequestBuilder.withUri(K_URI_VR).addAction(Action._new).getMock(), 
					null);
	}

	@Test
	public void t00_onLoad_check() {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		
		Param<String> pTrigger = vp_main.findParamByPath("/triggerOrder");
		assertNotNull(pTrigger);
		
		assertFalse(vp_main.findParamByPath("/tA").isVisible());
		assertNull(vp_main.findParamByPath("/tB").getState());
		assertFalse(vp_main.findParamByPath("/tC").isVisible());
	}
	
	@Test
	public void t01_trigger() {
		Object controllerResp_new = createNew_VR();
		Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		
		Param<String> pTrigger = vp_main.findParamByPath("/triggerOrder");
		pTrigger.setState("Y");
		
		assertTrue(vp_main.findParamByPath("/tA").isVisible());
		assertEquals("triggered", vp_main.findStateByPath("/tB"));
		assertTrue(vp_main.findParamByPath("/tC").isVisible());
	}
	
}
