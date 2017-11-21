/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandPathVariableResolverTest  extends AbstractFrameworkIngerationPersistableTests {

	
	@Test
	public void t0_nestedConfigPath() {
		Param<?> p = excuteNewConfig();
		assertNotNull(p);
		
		Param<String> testParam = p.findParamByPath("/testParam");
		assertNotNull(testParam);
		testParam.setState("test");
		
		Param<String> testParam2 = p.findParamByPath("/testParam2");
		assertNotNull(testParam2);
		testParam2.setState("testParam");
		
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT+"/paramConfigWithNestedPath")
				.addAction(Action._get)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		
		assertNotNull(resp);
		
		Param<String> testParam3 = p.findParamByPath("/testParam3");
		assertNotNull(testParam2);
		
		assertEquals("test", testParam3.getState());
		
		
	}
	
	private Param<?> excuteNewConfig() {
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
}
