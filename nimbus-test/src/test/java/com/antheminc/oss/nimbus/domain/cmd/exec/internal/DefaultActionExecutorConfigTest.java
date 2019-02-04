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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 * 
 */
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorConfigTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private DomainConfigBuilder domainConfigBuilder;
	
	@Autowired
	private MockMvc mvc;
	
	private MockHttpServletRequest createRequest(String domainPath, Action a) {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(domainPath)
				.addAction(a).getMock();
		
		return req;
	}

	@Test
	@WithMockUser(username="user", password="pwd")
	public void t00_json() throws Exception {
		 
		mvc.perform(post(createRequest(VIEW_PARAM_ROOT, Action._config).getRequestURI())
				.with(csrf())
					.content("{}")
					.contentType(APPLICATION_JSON_UTF8))
	               	.andExpect(status().isOk())
	               	.andExpect(jsonPath("$.result.0.result.outputs[0].value", IsNull.notNullValue()))
	               	.andReturn()
	               	.getResponse()
	               	.getContentAsString()
	               ;
	}
	
	@Test
	public void t01_get_config_noRefId_domainRoot() {
		Object resp = controller.handlePost(createRequest(VIEW_PARAM_ROOT, Action._config), null);
		assertNotNull(resp);
		
		ParamConfig<?> pConfig = ExtractResponseOutputUtils.extractOutput(resp);
		assertSame(domainConfigBuilder.getRootDomain(VIEW_DOMAIN_ALIAS), pConfig.getType().findIfNested().getModelConfig());
	}
	
//	@Test
//	public void t01_get_config_noRefId_domainRoot_action_get() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_update() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_replace() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_delete() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_nav() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_process() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_search() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t01_get_config_noRefId_nestedDomain() {
//		
//	}
	
}
