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
package com.antheminc.oss.nimbus.domain.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.bpm.activiti.ActivitiProcessFlow;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BPMGatewayTests extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	BPMGateway bpmGateway;
	
	@Autowired CommandMessageConverter converter;
	
	@Autowired ModelRepositoryFactory repositoryFactory;
	
	@Autowired ProcessRepository processRepo;
	
	protected static final String BPM_ERR_DOMAIN_ALIAS = "testbpmfailmodel";
	protected static final String BPM_ERR_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_ERR_DOMAIN_ALIAS;	

	@Test
	@SuppressWarnings("unchecked")
	public void t01_stateless_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_CORE_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri(BPM_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._process)
				.addParam("fn", "_bpm")
				.addParam("processId", "statelessbpmtest")
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request2, null);
		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(BPM_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		assertEquals(response.findStateByPath("/targetParameter"),"Assigned");
	}
	

	/**
	 * The class BPMStatefulTestModel has two parameters; parameterBeforeHumanTask and parameterAfterHumanTask
	 * The bpm lifecyle (bpmstatefulmodel.bpmn20.xml)checks for assignment of parameterBeforeHumanTask and stops if the parameter is not assigned.
	 * Once parameterBeforeHumanTask is set to a value, it should trigger the bpmn which would subsequently set parameterAfterHumanTask
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void t01_stateful_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_SF_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		
		//  Both the paramters shuould be null now
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(BPM_SF_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		assertNull(response.findStateByPath("/parameterBeforeHumanTask"));
		assertNull(response.findStateByPath("/parameterAfterHumanTask"));
		
		// Set the value of parameterBeforeHumanTask. This should trigger the bpmn to complete the human task and progress to assign value for parameterAfterHumanTask
		String updateUri = BPM_SF_PARAM_ROOT + ":"+domainRoot_refId+"/parameterBeforeHumanTask";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Assigned"));	
		
		//Fetch the model again to make sure the bpmn progressed and set the value for parameterAfterHumanTask
		request3 = MockHttpRequestBuilder.withUri(BPM_SF_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		response = (Param<?>)holder.getState().getSingleResult();
		
		// ********************** UNVOMMENT THE LINE BELOW ****************//
		assertEquals("Assigned", response.findStateByPath("/parameterAfterHumanTask"));	
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void t02_coretoview_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_CV_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		String updateUri = BPM_CV_PARAM_ROOT + ":"+domainRoot_refId+"/.m/coreParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Assigned"));	
		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(BPM_CV_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		assertEquals(response.findStateByPath("/viewResultParameter"),"Complete");
	}		
	
	@Test
	@SuppressWarnings("unchecked")
	public void t03_duplicate_task_creation_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_DP_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		
		// Set the value of parameterBeforeHumanTask. This should trigger the bpmn to complete the human task and progress to assign value for parameterAfterHumanTask
		String updateUri = BPM_DP_PARAM_ROOT + ":"+domainRoot_refId+"/triggerParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Step1"));	
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Step2"));	
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Complete"));	
		
		
		String getUri = BPM_DP_PARAM_ROOT + ":"+domainRoot_refId;
		MockHttpServletRequest request5 = MockHttpRequestBuilder.withUri(getUri)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request5, null);
		TestTaskContainerModel model = (TestTaskContainerModel)((Param)((MultiOutput)holder.getState()).getSingleResult()).getState();
		assertEquals("true", model.getTaskProgressed());
	
	}	
	
	@Test
	@SuppressWarnings("unchecked")
	public void t04_execution_fail() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_ERR_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		Param param = (Param)holder.getState().getSingleResult();
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)param.getRootExecution().getState()).getFlow();
		assertEquals("usertask1",processFlow.getActiveTasks().get(0));
		
		param.findParamByPath("/parameter1").setState("1");
		assertEquals("usertask2",processFlow.getActiveTasks().get(0));
		
		try {
			param.findParamByPath("/parameter2").setState("1");
		} catch (Exception e) {
			assertEquals("usertask2",processFlow.getActiveTasks().get(0));
		}
		
		ActivitiProcessFlow pf2 = processRepo._get(domainRoot_refId, ActivitiProcessFlow.class, "testbpmfailmodel_processFlow");
		assertEquals("usertask2",pf2.getActiveTasks().get(0));
		
	}
		
	
}
