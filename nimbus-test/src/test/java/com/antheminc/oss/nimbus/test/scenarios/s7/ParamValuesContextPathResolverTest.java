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
package com.antheminc.oss.nimbus.test.scenarios.s7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s7.core.S7C_CoreMain;
import com.antheminc.oss.nimbus.test.scenarios.s7.core.S7C_CoreMain.S7C_CoreStatic;
import com.antheminc.oss.nimbus.test.scenarios.s7.view.S7V_ViewMain;

/**
 * @author Swetha Vemuri
 * @author Rakesh Patel
 * <br>Test class to verify dynamic context path resolver for @Values and @ValuesConditional
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamValuesContextPathResolverTest extends AbstractFrameworkIntegrationTests {
	
	public static String VIEW_ROOT = PLATFORM_ROOT + "/s7v_main";
	
	public static final Long CORE_REF_ID_1 = new Long(1);
	public static final Long CORE_REF_ID_2 = new Long(2);
	public static final Long CORE_REF_ID_3 = new Long(3);
	
	@Autowired SessionProvider sessionProvider;
	
	@Test
	public void t00_paramvalues_static_context_resolve() {
		S7C_CoreMain s7c_main_1 = new S7C_CoreMain();
		s7c_main_1.setId(CORE_REF_ID_1);
		s7c_main_1.setAttr1_clone("test_0");
		mongo.insert(s7c_main_1, "s7c_main");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_1)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		actual.findParamByPath("/attr1").setState("test_0");
		Param<String> v_attr_values_2_param = actual.findParamByPath("/v_attr_values_2");
		assertNotNull(v_attr_values_2_param);
		List<ParamValue> values = v_attr_values_2_param.getValues();
		assertNotNull(values);
		assertEquals(1,values.size());
		
	}
	
	@Test
	public void t01_paramvalues_dynamic_context_resolve_conditional() {
		S7C_CoreMain s7c_main_1 = new S7C_CoreMain();
		s7c_main_1.setId(CORE_REF_ID_2);
		s7c_main_1.setAttr1_clone("test_1");
		mongo.insert(s7c_main_1, "s7c_main");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_2)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		actual.findParamByPath("/attr1").setState("test_1");
		Param<String> v_attr_values_2_param = actual.findParamByPath("/v_attr_values_2");
		assertNotNull(v_attr_values_2_param);
		List<ParamValue> values = v_attr_values_2_param.getValues();
		assertNotNull(values);
		assertEquals(1,values.size());
		
	}
	
	@Test
	public void t02_paramvalues_dynamic_context_resolve_get() {
		S7C_CoreMain s7c_main_1 = new S7C_CoreMain();
		s7c_main_1.setId(CORE_REF_ID_3);
		s7c_main_1.setAttr1_clone("test_2");
		mongo.insert(s7c_main_1, "s7c_main");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addRefId(CORE_REF_ID_3)
				.addAction(Action._get)
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		Param<String> v_attr_values_2_param = actual.findParamByPath("/attr3");
		assertNotNull(v_attr_values_2_param);
		List<ParamValue> values = v_attr_values_2_param.getValues();
		assertNotNull(values);
		assertEquals(1,values.size());
		
	}
	
	@Test
	public void t03_paramvalues_static_new() {
		S7C_CoreStatic coreStatic = new S7C_CoreStatic();
		coreStatic.setId(new Random().nextLong());
		coreStatic.setStaticAttr("test_01");
		mongo.insert(coreStatic, "s7c_corestatic");
		
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new)
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		Param<String> v_attr_values_param = actual.findParamByPath("/attr4");
		assertNotNull(v_attr_values_param);
		List<ParamValue> values = v_attr_values_param.getValues();
		assertNotNull(values);
		assertEquals(1,values.size());
		
	}
	
	//TODO [Rakesh] the @Values url does not support <! !>  onload of param (WIP)
	@Ignore
	public void t04_paramvalues_dynamic_context_resolve_new() {
		Object s7v_newResp = controller.handleGet(
				MockHttpRequestBuilder.withUri(VIEW_ROOT)
				.addAction(Action._new)
				.addParam("fn", "_initEntity")
				.addParam("target", "/.m/attr1_clone")
				.addParam("json", "\"test_2\"")
				.addParam("b", "$execute").getMock(),
				null);
		assertNotNull(s7v_newResp);	
		Param<S7V_ViewMain> actual = ExtractResponseOutputUtils.extractOutput(s7v_newResp, 0);
		assertNotNull(actual);
		//actual.findParamByPath("/attr3").setState("test_1");
		Param<String> v_attr_values_2_param = actual.findParamByPath("/attr3");
		assertNotNull(v_attr_values_2_param);
		List<ParamValue> values = v_attr_values_2_param.getValues();
		assertNotNull(values);
		assertEquals(1,values.size());
		
	}
}
