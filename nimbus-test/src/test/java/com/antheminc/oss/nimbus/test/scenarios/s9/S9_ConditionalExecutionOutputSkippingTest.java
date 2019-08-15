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
package com.antheminc.oss.nimbus.test.scenarios.s9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S9_ConditionalExecutionOutputSkippingTest extends AbstractFrameworkIntegrationTests {

	public static String VIEW_ROOT = PLATFORM_ROOT + "/s9v_main";
	
	@Test
	public void t00_init() throws Exception {
		
		Object controllerResp_new = controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new).getMock(), null);
		
		Assert.assertNotNull(ExtractResponseOutputUtils.extractOutput(controllerResp_new));
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	public void t01_configAndActivateConditional() throws Exception {
		
		// Create a new entity
		Object controllerResp_new = controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new).getMock(), null);
		
		Param<?> p_view = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Assert.assertNotNull(p_view);
		final Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		// Validate v2 is inactive
		assertFalse(p_view.findParamByPath("/vp/vt/vs/vf/v2").isActive());
		
		// Build the /event/notify request object
		MockHttpServletRequest req_v1_eventNotify = MockHttpRequestBuilder.withUri(PLATFORM_ROOT)
			.addNested("/event/notify")
			.getMock();
		
		// Build the model event that updates v1 with "code1"
		ModelEvent<String> modelEvent_v1 = new ModelEvent<>();
		modelEvent_v1.setId("/s9v_main:"+refId+"/vp/vt/vs/vf/v1");
		modelEvent_v1.setType(Action._update.name());
		modelEvent_v1.setPayload(json.write("code1").getJson());
		
		// Make the call
		Object controllerResp_q1 = controller.handleEventNotify(req_v1_eventNotify, modelEvent_v1);
		List<CommandExecution.Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(controllerResp_q1).getState()).getOutputs();
		assertNotNull(outputs);
		
		// Validate the correct number of outputs came back
		assertEquals(2, outputs.size());
		
		// Validate that v2 is active
		Param<String> p_v2 = (Param<String>) outputs.get(1).getValue();
		assertEquals("/s9v_main/vp/vt/vs/vf/v2", p_v2.getPath());
		assertTrue(p_v2.isActive());
	}
}