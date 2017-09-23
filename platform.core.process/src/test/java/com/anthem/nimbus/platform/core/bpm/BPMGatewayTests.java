/**
 * 
 */
package com.anthem.nimbus.platform.core.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
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

	@Test
	@SuppressWarnings("unchecked")
	public void t01_get_core() {
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
	
}
