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
package com.antheminc.oss.nimbus.domain.model.state;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Tony Lopez
 *
 */
public class ParamStateHolderTests extends AbstractFrameworkIngerationPersistableTests {

	private Long refId;
	
	@Before
	public void init() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addAction(Action._new).getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handlePost(request, null);
		this.refId = response.getState().getRootDomainId();
	}
	
	@Test
	public void testCollectionIsEmpty() throws JsonProcessingException {
		String payload = this.om.writeValueAsString(Arrays.asList("change"));
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(this.refId)
				.addNested("/paramStateHolders/simpleCollection1")
				.addAction(Action._update)
				.getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handlePost(request, payload);
		Param<String> targetParam = ParamUtils.extractResponseByParamPath(response, "/p1");
		Assert.assertTrue(targetParam.isActive());
	}
	
	@Test
	public void testStringIsEmpty() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(this.refId)
				.addNested("/paramStateHolders/string1")
				.addAction(Action._process)
				.addParam("fn", "_set").addParam("value", "change")
				.getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handlePost(request, null);
		Param<String> targetParam = ParamUtils.extractResponseByParamPath(response, "/p2");
		Assert.assertTrue(targetParam.isActive());
	}
	
	@Test
	public void testObjectIsEmpty() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(this.refId)
				.addNested("/paramStateHolders/object1")
				.addAction(Action._process)
				.addParam("fn", "_set").addParam("value", "change")
				.getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handlePost(request, null);
		Param<String> targetParam = ParamUtils.extractResponseByParamPath(response, "/p3");
		Assert.assertTrue(targetParam.isActive());
	}
}
