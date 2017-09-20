/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateUpdateEventHandlerTest extends AbstractFrameworkIngerationPersistableTests {
 
	@Test
	public void t03_WhenCaseCancelled_ThenCancelAllOpenTasks() {
		MockHttpServletRequest newReq = MockHttpRequestBuilder.withUri(CORE_ASSOCIATEDPARAM_ROOT)
				.addAction(Action._new)
				.addParam("fn", "_initEntity")
				.addParam("target", "entityId")
				.addParam("json", "\""+domainRoot_refId+"\"")
				.getMock();
		
		Object resp = controller.handleGet(newReq, null);
		assertNotNull(resp);
		
		final MultiOutput extractedResp = MultiOutput.class.cast(Holder.class.cast(resp).getState());
		
		assertNotNull(extractedResp);
		assertNotNull(extractedResp.getOutputs());
		
		MockHttpServletRequest newReq2 = MockHttpRequestBuilder.withUri(VIEW_CEU_PARAM_ROOT+"/action_updateStatus")
				.addAction(Action._get)
				.addParam("rawPayload", "\""+domainRoot_refId+"\"")
				.getMock();
		
		Object resp2 = controller.handleGet(newReq2, null);
		assertNotNull(resp2);
		
		final MultiOutput extractedResp2 = MultiOutput.class.cast(Holder.class.cast(resp2).getState());
		
		assertNotNull(extractedResp2);
		assertNotNull(extractedResp2.getOutputs());
		
		
		
		// TODO: add more asserts ...
	}
}
