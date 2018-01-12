/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.core.domain.command.execution;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.StateType.MappedTransient;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultTransientParamState;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;
 
 
/**
 * @author Soham Chakravarti
 * @author Sandeep Mantha
 * @author Rakesh Patel
*/
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorNewTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void t00_json() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();

		mvc.perform(post(req.getRequestURI()).content("{}").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.0.result.outputs[0].value", IsNull.notNullValue())).andReturn()
				.getResponse().getContentAsString();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void t01_ruleExecution() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addAction(Action._new).getMock();

		final Object resp = controller.handleGet(req, null);
		Object ob = ExtractResponseOutputUtils.extractOutput(resp);
		assertNotNull(ob);

		final MockHttpServletRequest fetchModifiedCoreDomain = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT)
				.addAction(Action._search).addParam("fn", "query").getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>) controller.handlePost(fetchModifiedCoreDomain, null);
		MultiOutput output = holder.getState();
		List<SampleCoreEntity> core = (List<SampleCoreEntity>) output.getSingleResult();
		assertEquals(core.size(), 2);
		assertEquals(core.get(0).getAttr_String(), "coreRuleUpdate");
		assertEquals(core.get(1).getAttr_String(), "coreRuleUpdate");

	}

	/**
	 * Test case added by Tony Lopez (AF42192) to resolve issue NIM-8531.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@Ignore
	public void t02_transientParamState_isTypeModelPresent() {

		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addNested("/page_red/tile/vt_attached_convertedNestedEntity")
				.addAction(Action._get)
				.getMock();
		
		final Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		assertNotNull(resp);
		
		final MappedDefaultTransientParamState paramState = (MappedDefaultTransientParamState) resp.getState().getOutputs().get(0).getValue();
		assertNotNull(((MappedTransient) paramState.getType()).getModel());
		
		// TODO - Validate the expected model
	}
}