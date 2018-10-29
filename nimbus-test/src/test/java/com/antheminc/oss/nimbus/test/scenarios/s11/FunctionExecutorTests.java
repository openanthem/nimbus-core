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
package com.antheminc.oss.nimbus.test.scenarios.s11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

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
import com.antheminc.oss.nimbus.test.scenarios.s11.core.FuncHandlerTestModel;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionExecutorTests extends AbstractFrameworkIngerationPersistableTests {

	protected static final String BPM_ERR_DOMAIN_ALIAS = "testbpmfailmodel";
	protected static final String BPM_ERR_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_ERR_DOMAIN_ALIAS;	

	@Test
	@SuppressWarnings("unchecked")
	public void t01_initEntity() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest/_new?fn=_initEntity&target=/parameter1&json=\"Test&1234\"").getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Param param = (Param)holder.getState().getSingleResult();
		assertEquals("Value", param.findStateByPath("/parameter2"));
		assertEquals("Test&1234", param.findStateByPath("/parameter1"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void t02_set_functionHandler() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId+"/parameter2")
				.addAction(Action._process)
				.addParam("fn", "_set")
				.addParam("value", "Value")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId)
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);

		Param param = (Param)holder.getState().getSingleResult();
		assertNull(param.findStateByPath("/parameter1"));
		assertEquals("Value", param.findStateByPath("/parameter2"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void t03_search_example() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId+"/parameter1")
				.addAction(Action._process)
				.addParam("fn", "_set")
				.addParam("value", "New-Value")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId)
				.addAction(Action._search)
				.addParam("fn", "example")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);

		List<FuncHandlerTestModel> obj = (List<FuncHandlerTestModel>)holder.getState().getSingleResult();
		assertTrue(obj.size() > 0);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void t04_search_query() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId+"/parameter1")
				.addAction(Action._process)
				.addParam("fn", "_set")
				.addParam("value", "New-Value")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId)
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", "sample_functest.parameter1.eq('New-Value')")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);

		List<FuncHandlerTestModel> obj = (List<FuncHandlerTestModel>)holder.getState().getSingleResult();
		assertTrue(obj.size() > 0);
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void t05_nav() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
		
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sample_functest:"+domainRoot_refId+"/parameter1")
				.addAction(Action._nav)
				.addParam("pageId", "page1")
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		
		Object pageId = holder.getState().getSingleResult();
		assertEquals("page1", pageId);
	}

}
		
	

