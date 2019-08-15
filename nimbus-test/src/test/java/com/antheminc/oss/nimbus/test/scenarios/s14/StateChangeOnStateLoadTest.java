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
package com.antheminc.oss.nimbus.test.scenarios.s14;

import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateChangeOnStateLoadTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	SessionProvider sessionProvider;

	@Test
	@SuppressWarnings("unchecked")
	public void t01_initEntity() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/stchangeonstloadview")
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Param param = (Param)holder.getState().getSingleResult();
		sessionProvider.removeAttribute("{/stchangeonstloadview:1}");
		request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/stchangeonstloadview:1")
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		param = (Param)holder.getState().getSingleResult();
		assertNotNull(param.findStateByPath("/inline_para/parameter4"));
	}
	
}
		
	

