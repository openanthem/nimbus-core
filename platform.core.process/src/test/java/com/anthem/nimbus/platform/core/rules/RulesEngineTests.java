/**
 * 
 */
package com.anthem.nimbus.platform.core.rules;

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
public class RulesEngineTests extends AbstractFrameworkIngerationPersistableTests {

	@Test
	@SuppressWarnings("unchecked")
	public void t01_core_rules() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(RULE_CORE_PARAM_ROOT)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		String domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		

		String updateUri = RULE_CORE_PARAM_ROOT + ":"+domainRoot_refId+"/triggerParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.convert("Start"));		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(RULE_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		Param<?>  response = (Param<?>)holder.getState().getSingleResult();
		assertEquals(response.findStateByPath("/triggeredParameter"),"Triggered");
	}	
	
}
