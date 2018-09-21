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
package com.antheminc.oss.nimbus.test.scenarios.s12;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityInitilizationTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	@SuppressWarnings("unchecked")
	public void t01_initEntity() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/entityinittest")
					.addAction(Action._new)
					.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Param param = (Param)holder.getState().getSingleResult();
		
		assertEquals("Value2", param.findStateByPath("/inline_para/parameter2"));
		assertEquals("Value3", param.findStateByPath("/inline_para/parameter3"));
		
		assertEquals("Value2", param.findStateByPath("/file_para/parameter2"));
		assertEquals("Value3", param.findStateByPath("/file_para/parameter3"));
		
		assertEquals("Value2", param.findStateByPath("/groovy_para/parameter2"));
		assertEquals("Value3", param.findStateByPath("/groovy_para/parameter3"));
		assertEquals("Value4", param.findStateByPath("/groovy_para/parameter4"));
	}
	
}
		
	

