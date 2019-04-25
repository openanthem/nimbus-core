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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultParamState.MappedLeafState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultTransientParamState;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleEntity;
 
 
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

	@WithMockUser(username="user", password="pwd")
	@Test
	public void t00_json() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();

		mvc.perform(post(req.getRequestURI()).content("{}")
				.with(csrf())
				.contentType(APPLICATION_JSON_UTF8))
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
	 * Test case added by Tony Lopez to resolve issue NIM-8531.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@Ignore
	public void t02_transientParamState_isTypeModelPresent() {
		
		// Test is modeled after CoverSheet.addAllergy @Config.
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addNested("/page_red/tile/modal/section/vf_attached_convertedNestedEntity")
				.addAction(Action._get)
				.addParam(Constants.KEY_FUNCTION.code, "param")
				.addParam(Constants.KEY_FN_PARAM_ARG_EXPR.code,  "assignMapsTo('../.m/attr_list_3_NestedEntity')")
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		assertNotNull(resp);
		
		// Skipping _process?fn=_setByRule&rule=togglemodal, not needed for test.
		
		req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addNested("/page_red/tile/modal/section/vf_attached_convertedNestedEntity")
				.addAction(Action._update)
				.getMock();
		
		resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		assertNotNull(resp);
		
		// Validate the response
		MappedDefaultTransientParamState form_paramState = (MappedDefaultTransientParamState) resp.getState().getOutputs().get(1).getValue();
		MappedDefaultModelState modelState_elem = (MappedDefaultModelState) ((StateType.Nested) form_paramState.getType()).getModel();
		MappedLeafState leafState_elem = (MappedLeafState) modelState_elem.getParams().get(2);
		StateType stateType_elem = leafState_elem.getType();
		// Not confident this is correct. But the UI is expecting type to have model, which is only
		// available from StateType.Nested and child entities. Need to validate the expected
		// behavior here.
		assertTrue(StateType.Nested.class.isAssignableFrom(stateType_elem.getClass()));
		
		// TODO: Once stateType_elem has accessible model, validate the validations are coming through.
	}
	
	@Test @Ignore
	public void t03_nestedGrid() throws Exception {
		Long refId = createOrGetDomainRoot_RefId();
		Long sampleEntityId = createOrGetSampleEntity_RefId();
		
		MockHttpServletRequest req_updateViewBy = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_updateModel = controller.handlePost(req_updateViewBy,"\"view1\"");
		assertNotNull(resp_updateModel);
		
		MockHttpServletRequest req_getGrid1_Results = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSampleGrid1/sampleGrid1").addAction(Action._get).getMock();
		Object resp_GridResults = controller.handleGet(req_getGrid1_Results, refId.toString());
		assertNotNull(resp_GridResults);
		
		MockHttpServletRequest req_update2ViewBy = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_update2Model = controller.handlePost(req_update2ViewBy,"\"view2\"");
		assertNotNull(resp_update2Model);
		
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core.getId());
		
		MockHttpServletRequest req_root = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
										 .addAction(Action._get).getMock();
		Object resp_root = controller.handleGet(req_root, refId.toString());
		Param<?> rootEntity = ExtractResponseOutputUtils.extractOutput(resp_root);
		assertNotNull(rootEntity);
		assertNull(rootEntity.findParamByPath("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/.m").getState());
		
		MockHttpServletRequest req_getGrid2_Results = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2").addAction(Action._get).getMock();
		Object resp_Grid2Results = controller.handleGet(req_getGrid2_Results, refId.toString());
		assertNotNull(resp_Grid2Results);
		
		assertNotNull(rootEntity.findParamByPath("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/.m").getState());
		
		SampleEntity innerGridCore = mongo.findById(sampleEntityId, SampleEntity.class, "sample_entity");
		assertNotNull(innerGridCore.getId());
		
		MockHttpServletRequest req_getGrid2RowExpander = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/0/childList/innerGrid").addAction(Action._get).getMock();
		Object resp_InnerGridResults = controller.handleGet(req_getGrid2RowExpander, refId.toString());
		assertNotNull(resp_InnerGridResults);
		
		MockHttpServletRequest req_update3ViewBy = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_update3Model = controller.handlePost(req_update3ViewBy,"\"view1\"");
		assertNotNull(resp_update3Model);
	}
	
	@Test @Ignore
	public void t04_nestedGrid() throws Exception {
		Long refId = createOrGetDomainRoot_RefId();
		Long sampleEntityId = createOrGetSampleEntity_RefId();
		final String VIEW_ROOT_WITHOUT_MAPPEDMODEL = PLATFORM_ROOT + "/sample_withoutmodel";
		
		MockHttpServletRequest req_updateViewBy = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_updateModel = controller.handlePost(req_updateViewBy,"\"view1\"");
		assertNotNull(resp_updateModel);
		
		MockHttpServletRequest req_getGrid1_Results = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSampleGrid1/sampleGrid1").addAction(Action._get).getMock();
		Object resp_GridResults = controller.handleGet(req_getGrid1_Results,null);
		assertNotNull(resp_GridResults);
		
		MockHttpServletRequest req_update2ViewBy = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_update2Model = controller.handlePost(req_update2ViewBy,"\"view2\"");
		assertNotNull(resp_update2Model);
		
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core.getId());
		
		MockHttpServletRequest req_root = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
										 .addAction(Action._get).getMock();
		Object resp_root = controller.handleGet(req_root,null);
		Param<?> rootEntity = ExtractResponseOutputUtils.extractOutput(resp_root);
		assertNotNull(rootEntity);
		assertNull(rootEntity.findParamByPath("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/.m").getState());
		
		MockHttpServletRequest req_getGrid2_Results = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2").addAction(Action._get).getMock();
		Object resp_Grid2Results = controller.handleGet(req_getGrid2_Results,null);
		assertNotNull(resp_Grid2Results);
		
		assertNotNull(rootEntity.findParamByPath("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/.m").getState());
		
		SampleEntity innerGridCore = mongo.findById(sampleEntityId, SampleEntity.class, "sample_entity");
		assertNotNull(innerGridCore.getId());
		
		MockHttpServletRequest req_getGrid2RowExpander = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/0/childList/innerGrid").addAction(Action._get).getMock();
		Object resp_InnerGridResults = controller.handleGet(req_getGrid2RowExpander,null);
		assertNotNull(resp_InnerGridResults);
		
		MockHttpServletRequest req_update3ViewBy = MockHttpRequestBuilder.withUri(VIEW_ROOT_WITHOUT_MAPPEDMODEL)
				.addNested("/page_orange/vtOrange/vsSwitchView/viewBy").addAction(Action._update).getMock();
		
		Object resp_update3Model = controller.handlePost(req_update3ViewBy,"\"view1\"");
		assertNotNull(resp_update3Model);
	}
	
	@Test
	public void t05_sampleBpmn() throws Exception {
		Long refId = createOrGetSampleEntityWithBPMN_RefId();
		
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertNotNull(core);
		
//		MockHttpServletRequest req_task1 = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refId)
//				.addNested("/currentTaskName").addAction(Action._get).getMock();
//		Object resp_task1 = controller.handleGet(req_task1,null);
//		assertNotNull(resp_task1);
//		Object respObj = ExtractResponseOutputUtils.extractOutput(resp_task1);
//		assertNotNull(respObj);
		
		MockHttpServletRequest req_evalBpmn = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_task1").addAction(Action._update).getMock();
		Object resp_evalBpmn = controller.handlePut(req_evalBpmn, null, converter.toJson("InProgress"));
		assertNotNull(resp_evalBpmn);
		
		MockHttpServletRequest req_evalBpmn2 = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_task1").addAction(Action._update).getMock();
		Object resp_evalBpmn2 = controller.handlePut(req_evalBpmn2, null, converter.toJson("Complete"));
		assertNotNull(resp_evalBpmn2);
		
		MockHttpServletRequest req_SampleTask2 = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refId)
				.addNested("/currentTaskName").addAction(Action._get).getMock();
		Object resp_SampleTask2 = controller.handleGet(req_SampleTask2,null);
		assertNotNull(resp_SampleTask2);
		DefaultParamState<?> respTaskObj = ExtractResponseOutputUtils.extractOutput(resp_SampleTask2);
		assertEquals(respTaskObj.getState(), "task2");

	}
}