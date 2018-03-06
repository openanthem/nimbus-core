/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.s4;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s4.view.S4Values;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S4_ValidateConditionalTest extends AbstractFrameworkIntegrationTests {

	public static String VIEW_ROOT = PLATFORM_ROOT + "/s4_MainCoreBackingObjectView";
	
	@Test
	public void t00_init() throws Exception {
		
		Object controllerResp_new = controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new).getMock(), null);
		
		Assert.assertNotNull(ExtractResponseOutputUtils.extractOutput(controllerResp_new));
	}
	
	@Test
	public void t01_checkQ2ValuesAfterQ1Update() throws Exception {
		
		// Create a new entity
		Object controllerResp_new = controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new).getMock(), null);
		
		Assert.assertNotNull(ExtractResponseOutputUtils.extractOutput(controllerResp_new));
		
		// Execute assign maps to call on param: addData
		Object controllerResp_addData_get = controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId("1")
				.addNested("/vpMain/vtMain/vsMain/vfMain/addData")
				.addAction(Action._get).getMock(), null);
		
		// Update the state of param: q1
		MockHttpServletRequest req_q1_eventNotify = MockHttpRequestBuilder.withUri(PLATFORM_ROOT)
			.addNested("/event/notify")
			.addAction(Action._replace)
			.getMock();
		
		ModelEvent<String> modelEvent_q1 = new ModelEvent<>();
		modelEvent_q1.setId("/s4_MainCoreBackingObjectView:1/vpMain/vtMain/addDataModal/vsAddDataModalBody/vfAddDataModalForm/q1/_replace");
		modelEvent_q1.setType(Action._replace.name());
		modelEvent_q1.setPayload("\"preference2\"");
		
		// Validate that q1's ValuesConditional was applied to param: q2.
		Object controllerResp_q1 = controller.handleEventNotify(req_q1_eventNotify, modelEvent_q1);
		
		List<CommandExecution.Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(controllerResp_q1).getState()).getOutputs();
		Assert.assertEquals(3, outputs.size());
		List<ParamValue> values = ((Param<?>) outputs.get(2).getValue()).getValues();
		Assert.assertEquals(3, values.size());
		Assert.assertEquals("preference2a", values.get(0).getCode());
		Assert.assertEquals("preference2b", values.get(1).getCode());
		Assert.assertEquals("preference2c", values.get(2).getCode());
	}
}
