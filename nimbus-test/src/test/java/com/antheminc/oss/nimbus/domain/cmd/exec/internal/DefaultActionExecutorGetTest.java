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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Tony Lopez
 *
 */
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorGetTest extends AbstractFrameworkIngerationPersistableTests {

	@SuppressWarnings("unchecked")
	@Test
	public void testStartBPMOnGet() {
		
		// Insert a new "sampleCore" object by invoking "_new" command like "/p/sampleCore/_new?rawPayload=xyz"
		MockHttpServletRequest coreNewRequest = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT)
				.addAction(Action._new).getMock();
		final Holder<MultiOutput> coreNewResponse = (Holder<MultiOutput>) controller.handleGet(coreNewRequest, null);
		Long refID = coreNewResponse.getState().findFirstParamOutputEndingWithPath("/sample_core").getRootDomainId();
		
		// Perform the "_get" operation on the view
		MockHttpServletRequest sampleTask2Request = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refID)
				.addAction(Action._get).getMock();
		Holder<MultiOutput> sampleTask2Response = (Holder<MultiOutput>) controller.handleGet(sampleTask2Request,null);
		assertNotNull(sampleTask2Response);
		
		// Execute preconditions for bpmn logic
		MockHttpServletRequest evalBpmn2Request = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addRefId(refID)
				.addNested("/attr_task1").addAction(Action._update).getMock();
		Holder<MultiOutput> evalBpmn2Response = (Holder<MultiOutput>) controller.handlePut(evalBpmn2Request, null, converter.toJson("Complete"));
		assertNotNull(evalBpmn2Response);
		
		// Validate the bpmn logic was executed
		Param<?> currentTaskNameParam = (Param<?>) evalBpmn2Response.getState().findFirstParamOutputEndingWithPath("/currentTaskName").getValue();
		assertEquals("task2", currentTaskNameParam.getState());
	}
}
