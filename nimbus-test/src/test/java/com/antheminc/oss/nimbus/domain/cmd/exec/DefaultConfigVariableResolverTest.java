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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Tony Lopez
 *
 */
public class DefaultConfigVariableResolverTest extends AbstractFrameworkIngerationPersistableTests {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLetStaticReplacementSingle() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p1")
				.addAction(Action._get)
				.getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<?> p0 = (Param<?>) resp.getState().findFirstParamOutputEndingWithPath("/p0").getValue();
		
		assertEquals("p1-did-it", p0.getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLetStaticReplacementMultiple() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p2")
				.addAction(Action._get)
				.getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<?> p0 = (Param<?>) resp.getState().findFirstParamOutputEndingWithPath("/p0").getValue();
		
		assertEquals("p2-did-it", p0.getState());
	}
	
	@Test
	public void testLetStaticReplacementUndefined() {
		thrown.expect(NumberFormatException.class);
		thrown.expectMessage("For input string: \"1null\"");
		
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p3")
				.addAction(Action._get)
				.getMock();
		
		controller.handleGet(req, null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLetStaticReplacementWithDetourConfigs() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p4")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		// TODO Output not generated. Why?
		Param<?> p0 = resp.getState().getContext().getQuadModel().getView().findParamByPath("/configVariables/p0");
		
		assertEquals("p4-did-it", p0.getState());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLetStaticReplacementWithCol() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p8")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		// TODO Output not generated. Why?
		Param<String> p0 = resp.getState().getContext().getQuadModel().getView().findParamByPath("/configVariables/p0");

		assertEquals("p8-did-it", p0.getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLetSpelReplacement() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p0")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<?> p0 = (Param<?>) resp.getState().findFirstParamOutputEndingWithPath("/p0").getValue();

		p0.findParamByPath("/../p9").setState("0");
		
		MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configVariables/p9")
				.addAction(Action._get)
				.getMock();
		controller.handleGet(req2, null);
		
		assertEquals("p9-did-it", p0.getState());
	}
}
