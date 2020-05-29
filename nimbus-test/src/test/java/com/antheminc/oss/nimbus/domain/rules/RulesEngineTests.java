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
package com.antheminc.oss.nimbus.domain.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 * @author Swetha Vemuri
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
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		

		String updateUri = RULE_CORE_PARAM_ROOT + ":"+domainRoot_refId+"/triggerParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Start"));		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(RULE_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		Param<?>  response = (Param<?>)holder.getState().getSingleResult();
		assertEquals("Triggered", response.findStateByPath("/triggeredParameter"));
	}	
	
	@SuppressWarnings("unchecked")
	@Test
	public void t02_core_dtable_rules() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(DTABLE_CORE_PARAM_ROOT)
				.addAction(Action._new)
				.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		assertNotNull(domainRoot_refId);
		
	
		String updateUri = DTABLE_CORE_PARAM_ROOT + ":"+domainRoot_refId+"/triggerParameter";
		MockHttpServletRequest request4 = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request4, converter.toJson("Start"));		
		MockHttpServletRequest request3 = MockHttpRequestBuilder.withUri(DTABLE_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request3, null);		
		Param<?>  response = (Param<?>)holder.getState().getSingleResult();
		assertEquals("Triggered", response.findStateByPath("/triggeredParameter"));
	}
}
