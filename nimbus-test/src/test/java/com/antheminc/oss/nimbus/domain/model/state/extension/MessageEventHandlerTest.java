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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Swetha Vemuri
 * This test case demonstrates the message reset in DefaultJsonParamSerializer
 */
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageEventHandlerTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	private MockMvc mvc;
	
	private MockHttpServletRequest createRequest(String domainPath, Action a) {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(domainPath)
				.addAction(a).getMock();
		
		return req;
	}
	
	@Test
	public void t00_messageState_json_get() throws Exception{
		domainRoot_refId = createOrGetDomainRoot_RefId();
		assertNotNull(domainRoot_refId);
		
		String uri; 
		MockHttpServletRequest req;
		uri = VIEW_PARAM_ROOT + ":"+domainRoot_refId+"/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox";
		req = createRequest(uri, Action._update);
		
		final Object update_resp = controller.handlePost(req, "101");
		Object ob = ExtractResponseOutputUtils.extractOutput(update_resp);
		assertNotNull(ob);
		uri = VIEW_PARAM_ROOT+":"+domainRoot_refId;
		// _get call the first time runs the message conditional on param and sets the message on testWarningTextBox to true
		Object root_getResp = controller.handleGet(createRequest(uri, Action._get), null);
		assertNotNull(root_getResp);
		Param<VRSampleViewRootEntity> view = ExtractResponseOutputUtils.extractOutput(root_getResp, 0);
		Param<?> testWarningTextBox_p = view.findParamByPath("/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox");
		assertNotNull(testWarningTextBox_p.getMessages());
		assertEquals(1, testWarningTextBox_p.getMessages().size());
		Message msg = testWarningTextBox_p.getMessages().stream()
			.filter(m -> StringUtils.equalsIgnoreCase("This is a Test Warning Message", m.getText())).findFirst().get();		
		assertEquals("This is a Test Warning Message",msg.getText());
		
		// to simulate the _get call with JsonSerializer
		mvc.perform(get(createRequest(uri, Action._get).getRequestURI())
				.contentType(APPLICATION_JSON_UTF8))
               	.andExpect(status().isOk())
               	.andExpect(jsonPath("$.result.0.result.outputs[0].value.type.model.params[4].type.model.params[0].type.model.params[0].type.model.params[0].type.model.params[0].message[0].text", IsNull.notNullValue()))
               	.andReturn()
               	.getResponse()
               	.getContentAsString()
               ;
		
		// After JSONSerialization, the message state is set to null
		assertNull(view.findParamByPath("/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox").getMessages());
	}
	
	@Test
	public void t02_messageState_json_onload() throws Exception{
		MockHttpServletRequest home_newReq = createRequest(VIEW_PARAM_ROOT, Action._new);
		mvc.perform(get(home_newReq.getRequestURI())
				.contentType(APPLICATION_JSON_UTF8))
               	.andExpect(status().isOk())
               	.andExpect(jsonPath("$.result.0.result.outputs[0].value.type.model.params[4].type.model.params[0].type.model.params[0].type.model.params[0].type.model.params[9].message[0].text", IsNull.notNullValue()))
               	.andReturn()
               	.getResponse()
               	.getContentAsString()
               ;
		
	}
}
