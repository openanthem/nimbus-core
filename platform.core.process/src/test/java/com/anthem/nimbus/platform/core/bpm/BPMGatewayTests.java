/**
 * 
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

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

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
	//@Test
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
		response.findParamByPath("/parameterBeforeHumanTask").setState("Assigned"); // This should now trigger the bpmn flow
		
		//Fetch the model again to make sure the bpmn progressed and set the value for parameterAfterHumanTask
		request3 = MockHttpRequestBuilder.withUri(BPM_SF_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		response = (Param<?>)holder.getState().getSingleResult();
		
		// ********************** UNVOMMENT THE LINE BELOW ****************//
		assertEquals("Assigned", response.findStateByPath("/parameterAfterHumanTask"));	
	}
		
	
}
