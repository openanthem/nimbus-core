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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.nav.PageNavigationResponse;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Tony Lopez
 *
 */
public class DefaultActionExecutorNavTest extends AbstractFrameworkIngerationPersistableTests {

	@Before
	public void init() {
		createOrGetDomainRoot_RefId();
	}
	
	@Test(expected = InvalidConfigException.class)
	public void testUnknownNavigation() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(domainRoot_refId)
				.addAction(Action._nav).getMock();
		this.controller.handleGet(request, null);
	}
	
	@Test
	public void testPageIdNavigation() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(domainRoot_refId)
				.addAction(Action._nav)
				.addParam(Constants.KEY_NAV_ARG_PAGE_ID.code, "page_green")
				.getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handleGet(request, null);
		PageNavigationResponse actual = (PageNavigationResponse) response.getState().getSingleResult();
		System.out.println(actual);
		Assert.assertEquals(PageNavigationResponse.Type.INTERNAL, actual.getType());
		Assert.assertEquals("page_green", actual.getPageId());
	}
	
	@Test
	public void testExternalRedirect() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(domainRoot_refId)
				.addAction(Action._nav)
				.addParam(Constants.KEY_NAV_ARG_REDIRECT_URL.code, "https://mywebsite.com")
				.getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handleGet(request, null);
		PageNavigationResponse actual = (PageNavigationResponse) response.getState().getSingleResult();
		Assert.assertEquals(PageNavigationResponse.Type.EXTERNAL, actual.getType());
		Assert.assertEquals("https://mywebsite.com", actual.getRedirectUrl());
	}
}
