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

import java.lang.annotation.Annotation;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandPathVariableResolverTest  extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	private CommandExecutorGateway commandExecutorGateway;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
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
		
		Long refId = p.findStateByPath("/id");
		
		execGet(refId, "/paramConfigWithNestedPath");
		
		Param<String> testParam3 = p.findParamByPath("/testParam3");
		assertNotNull(testParam2);
		
		assertEquals("test", testParam3.getState());
		
		execGet(refId, "/paramConfigWithNestedPath2");
		
		Param<String> testParam4 = p.findParamByPath("/testParam3");
		assertNotNull(testParam4);
		
		assertEquals("testtestParam", testParam4.getState());
		
		
	}
	
	@Test
	public void t1_environmentProperty() {
		String coreRoot = PLATFORM_ROOT+"/sample_cmd_test";
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(coreRoot)
				.addAction(Action._new)
				.getMock();
		Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		assertNotNull(p);
		
		String id = p.findStateByPath("/id").toString();
		
		req = MockHttpRequestBuilder.withUri(coreRoot+":"+id+"/action_setEnvProperty/_get").getMock();
		resp = controller.handleGet(req, null);
		
		assertEquals("key1", p.findStateByPath("/test_parameter1"));
		assertEquals("key2", p.findStateByPath("/test_parameter2"));

		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConfigPlaceholderSingle() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p1")
				.addAction(Action._get)
				.getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<?> p0 = (Param<?>) resp.getState().findFirstParamOutputEndingWithPath("/p0").getValue();
		
		assertEquals("p1-did-it", p0.getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConfigPlaceholderMultiple() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p2")
				.addAction(Action._get)
				.getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<?> p0 = (Param<?>) resp.getState().findFirstParamOutputEndingWithPath("/p0").getValue();
		
		assertEquals("p2-did-it", p0.getState());
	}
	
	@Test
	public void testConfigPlaceholderUndefined() {
		thrown.expect(NumberFormatException.class);
		thrown.expectMessage("For input string: \"1null\"");
		
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p3")
				.addAction(Action._get)
				.getMock();
		
		controller.handleGet(req, null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConfigPlaceholderWithDetourConfigs() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p4")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		// TODO Output not generated. Why?
		Param<?> p0 = resp.getState().getContext().getQuadModel().getView().findParamByPath("/configPlaceholders/p0");
		
		assertEquals("p4-did-it", p0.getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testConfigPlaceholderWithConfigConditional() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p5")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<String> p5 = (Param<String>) resp.getState().findFirstParamOutputEndingWithPath("/p5").getValue();
		
		p5.setState("changeit");
		
		Param<String> p0 = p5.findParamByPath("/../p0");
		assertEquals("p5-did-it", p0.getState());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testConfigPlaceholderWithCol() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/configPlaceholders/p8")
				.addAction(Action._get)
				.getMock();
		
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		// TODO Output not generated. Why?
		Param<String> p0 = resp.getState().getContext().getQuadModel().getView().findParamByPath("/configPlaceholders/p0");

		assertEquals("p8-did-it", p0.getState());
	}
	
	private void execGet(Long refId, String nestedPath) {
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT)
				.addRefId(refId)
				.addNested(nestedPath)
				.addAction(Action._get)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		
		assertNotNull(resp);
	}
	
	private Param<?> excuteNewConfig() {
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
	
	private static ConfigPlaceholder buildConfigPlaceholder(String name, String value) {
		return new ConfigPlaceholder() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return ConfigPlaceholder.class;
			}
			
			@Override
			public String value() {
				return value;
			}
			
			@Override
			public String name() {
				return name;
			}
		};
	}
}
