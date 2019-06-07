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
package com.antheminc.oss.nimbus.test.scenarios.s4;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.MyData;

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
		final Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		// Execute assign maps to call on param: S4_VRMainCoreBackingObjectView...VFMain.addData
		controller.handleGet(MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(refId)
				.addNested("/vpMain/vtMain/vsMain/vfMain/addData")
				.addAction(Action._get).getMock(), null);
		
		// Update the state of param: VFAddDataModalForm.q1
		MockHttpServletRequest req_q1_eventNotify = MockHttpRequestBuilder.withUri(PLATFORM_ROOT)
			.addNested("/event/notify")
			.addAction(Action._replace)
			.getMock();
		
		// Build the request that updates the state of VFAddDataModalForm.q1 with "preference2"
		// This should trigger @ValuesConditional to set the values of VFAddDataModalForm.q2 to Preference2Types.class
		ModelEvent<String> modelEvent_q1 = new ModelEvent<>();
		modelEvent_q1.setId("/s4_MainCoreBackingObjectView:"+refId+"/vpMain/vtMain/addDataModal/vsAddDataModalBody/vfAddDataModalForm/q1/_replace");
		modelEvent_q1.setType(Action._replace.name());
		modelEvent_q1.setPayload(json.write(MyData.Preference.preference2).getJson());
		
		// Make the call
		Object controllerResp_q1 = controller.handleEventNotify(req_q1_eventNotify, modelEvent_q1);
		List<CommandExecution.Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(controllerResp_q1).getState()).getOutputs();
		
		Param<?> q1 = null;
		Param<?> q2 = null;
		
		for(Output<?> o : outputs) {
			if(o.getValue() instanceof Param) {
				Param<?> po = (Param<?>)o.getValue();
				if(po.getPath().endsWith("q2")) {
					q2 = po;
					q1 = q2.findParamByPath("/../q1");
				}
			} 
		}
		assertNotNull(q1);
		assertNotNull(q2);
		
		// #1. Validate VFAddDataModalForm.q1 state change occurred
		Assert.assertEquals(MyData.Preference.preference2, q1.getState());
		
		// #2. Validate that q1's ValuesConditional was applied to param: VFAddDataModalForm.q2
		
		// TODO: Get the output related to the valuesconditional update
		
		// Validate that the new values are in place.
		Assert.assertEquals(3, q2.getValues().size());
		Assert.assertEquals("preference2a", q2.getValues().get(0).getCode());
		Assert.assertEquals("preference2b", q2.getValues().get(1).getCode());
		Assert.assertEquals("preference2c", q2.getValues().get(2).getCode());
	}
}
