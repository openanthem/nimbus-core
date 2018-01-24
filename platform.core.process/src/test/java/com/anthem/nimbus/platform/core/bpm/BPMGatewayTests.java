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
package com.anthem.nimbus.platform.core.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BPMGatewayTests extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	BPMGateway bpmGateway;
	
	@Autowired CommandMessageConverter converter;

	@Test
	@SuppressWarnings("unchecked")
	public void t01_stateless_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_CORE_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		String domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
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
		String domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
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
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Assigned"));	
		
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
		String domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		String updateUri = BPM_CV_PARAM_ROOT + ":"+domainRoot_refId+"/.m/coreParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Assigned"));	
		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(BPM_CV_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		assertEquals(response.findStateByPath("/viewResultParameter"),"Complete");
	}		
	
	@Test
	@SuppressWarnings("unchecked")
	public void t02_duplicate_task_creation_bpm() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(BPM_DP_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		String domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
		
		// Set the value of parameterBeforeHumanTask. This should trigger the bpmn to complete the human task and progress to assign value for parameterAfterHumanTask
		String updateUri = BPM_DP_PARAM_ROOT + ":"+domainRoot_refId+"/triggerParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Step1"));	
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Step2"));	
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Complete"));	
		
		
		String getUri = BPM_DP_PARAM_ROOT + ":"+domainRoot_refId;
		MockHttpServletRequest request5 = MockHttpRequestBuilder.withUri(getUri)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request5, null);
		TestTaskContainerModel model = (TestTaskContainerModel)((Param)((MultiOutput)holder.getState()).getSingleResult()).getState();
		assertEquals("true", model.getTaskProgressed());
	
	}	
		
	
}
