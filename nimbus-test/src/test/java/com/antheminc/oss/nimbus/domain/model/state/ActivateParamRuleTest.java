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
package com.antheminc.oss.nimbus.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrameworkIntegrationTestScenariosApplication.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActivateParamRuleTest  extends ActivateParamBaseTest {
	
	private static final String CORE_PARAM_PATH_q2 = "/sample_core/q2";
	private static final String CORE_PARAM_PATH_q2Level1 = "/sample_core/q2Level1";
	private static final String CORE_PARAM_PATH_q2Level1_nested_attr_String = CORE_PARAM_PATH_q2Level1 + "/nested_attr_String";
	
	private static final String CORE_PARAM_PATH_q4 = "/sample_core/q4";
	private static final String CORE_PARAM_PATH_q4Level1 = "/sample_core/q4Level1";
	private static final String CORE_PARAM_PATH_q4Level2 = "/sample_core/q4Level2";
	private static final String CORE_PARAM_PATH_q4_nested2_attr_String_1 = CORE_PARAM_PATH_q4 + "/nested2_attr_String_1";
	private static final String CORE_PARAM_PATH_q4_nested2_attr_String_2 = CORE_PARAM_PATH_q4 + "/nested2_attr_String_2";
	private static final String CORE_PARAM_PATH_q4Level1_nested2_attr_String_1 = CORE_PARAM_PATH_q4Level1 + "/nested2_attr_String_1";

	@Override
	protected String getSourceParamPath() {
	 	return CORE_PARAM_PATH_q2;
	}
	
	@Override
	protected String getTargetParamPath() {
		return CORE_PARAM_PATH_q2Level1;
	}
	
	protected String getTargetNestedAttribPath() {
		return CORE_PARAM_PATH_q2Level1_nested_attr_String;
	}
	
	@Test
	public void t00_init() {
		Param<?> q2 = _q.getRoot().findParamByPath(getSourceParamPath());
		Param<?> q2Level1 = _q.getRoot().findParamByPath(getTargetParamPath());
		Param<?> q2Level1_attrib = _q.getRoot().findParamByPath(getTargetNestedAttribPath());
		
		assertNull(q2.getState());
		assertNull(q2Level1.getState());
		assertNull(q2Level1_attrib.getState());
		
		assertTrue(q2.isActive());
		assertFalse(q2Level1.isActive());
		assertFalse(q2Level1_attrib.isActive());
	}
	
	@Test
	public void t01_rule_activate() {
		// verify initial state
		t00_init();
		
		Param<String> q2 = _q.getRoot().findParamByPath(getSourceParamPath());
		Param<?> q2Level1 = _q.getRoot().findParamByPath(getTargetParamPath());
		Param<?> q2Level1_attrib = _q.getRoot().findParamByPath(getTargetNestedAttribPath());
		
		final String K_q2 = "Y";
		q2.setState(K_q2);
		
		assertEquals(K_q2, q2.getState());
		assertNull(q2Level1.getState());
		assertNull(q2Level1_attrib.getState());
		
		assertTrue(q2.isActive());
		assertTrue(q2Level1.isActive());
		assertTrue(q2Level1_attrib.isActive());
	}
	
	@Test
	public void t02_rule_deactivate() {
		t01_rule_activate();
		
		Param<String> q2 = _q.getRoot().findParamByPath(getSourceParamPath());
		Param<?> q2Level1 = _q.getRoot().findParamByPath(getTargetParamPath());
		Param<String> q2Level1_attrib = _q.getRoot().findParamByPath(getTargetNestedAttribPath());
		
		// check for nested param reset
		q2Level1_attrib.setState("new nested @ "+new Date());
		
		final String K_q2 = "N";
		q2.setState(K_q2);
		
		assertEquals(K_q2, q2.getState());
		assertNull(q2Level1.getState());
		assertNull(q2Level1_attrib.getState());
		
		assertTrue(q2.isActive());
		assertFalse(q2Level1.isActive());
		assertFalse(q2Level1_attrib.isActive());
	}
	
	@Test
	public void t03_rule_multiple() {
		Param<String> q3 = _q.getRoot().findParamByPath("/sample_core/q3");
		Param<SampleCoreNestedEntity> q3Level1 = _q.getRoot().findParamByPath("/sample_core/q3Level1");
		Param<String> q3Level1_attrib = _q.getRoot().findParamByPath("/sample_core/q3Level1/nested_attr_String");
		Param<SampleCoreNestedEntity> q3Level2 = _q.getRoot().findParamByPath("/sample_core/q3Level2");
		Param<String> q3Level2_attrib = _q.getRoot().findParamByPath("/sample_core/q3Level2/nested_attr_String");
		
		assertNotNull(q3);
		assertNotNull(q3Level1);
		assertNotNull(q3Level1_attrib);
		assertNotNull(q3Level2);
		assertNotNull(q3Level2_attrib);
		
		assertNull(q3Level1_attrib.getState());
		assertNull(q3Level1.getState());
		assertNull(q3Level2_attrib.getState());
		assertNull(q3Level2.getState());
		assertNull(q3.getState());
		
		assertTrue(q3.isActive());
		assertFalse(q3Level1.isActive());
		assertFalse(q3Level1_attrib.isActive());
		assertFalse(q3Level2.isActive());
		assertFalse(q3Level2_attrib.isActive());
		
		// set 'A'
		final String K_A = "A";
		q3.setState(K_A);
		
		assertTrue(q3.isActive());
		assertTrue(q3Level1.isActive());
		assertTrue(q3Level1_attrib.isActive());
		assertFalse(q3Level2.isActive());
		assertFalse(q3Level2_attrib.isActive());
		
		// set 'A.Level1.attrib'
		final String K_A_Level1_Attrib = "A.Level1.attrib @" + new Date();
		q3Level1_attrib.setState(K_A_Level1_Attrib);
		assertSame(K_A_Level1_Attrib, q3Level1.getState().getNested_attr_String());
		
		// set 'B'
		final String K_B = "B";
		q3.setState(K_B);
		
		assertTrue(q3.isActive());
		assertFalse(q3Level1.isActive());
		assertFalse(q3Level1_attrib.isActive());
		assertTrue(q3Level2.isActive());
		assertTrue(q3Level2_attrib.isActive());
		
		assertNull(q3Level1_attrib.getState());
		assertNull(q3Level1.getState());
	}
	
	@Test
	public void t04_rule_activate_nested() {
		// verify initial state
		t00_init();
		
		Param<String> q4 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4);
		Param<?> q4Level1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level1);
		Param<?> q4Level2 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level2);
		Param<String> q4_attrib_1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4_nested2_attr_String_1);
		Param<String> q4_attrib_2 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4_nested2_attr_String_2);
		Param<?> q4Level1_attr_1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level1_nested2_attr_String_1);
		
		final String state_q4_attrib_1 = "Y";
		final String state_q4_attrib_2 = "Y";
		q4_attrib_1.setState(state_q4_attrib_1);
		q4_attrib_2.setState(state_q4_attrib_2);
		
		
		assertEquals(state_q4_attrib_1, q4_attrib_1.getState());
		assertEquals(state_q4_attrib_2, q4_attrib_2.getState());
		assertNull(q4Level1.getState());
		assertNull(q4Level2.getState());
		assertNull(q4Level1_attr_1.getState());
		
		assertTrue(q4.isActive());
		assertTrue(q4Level1.isActive());
		assertTrue(q4Level2.isActive());
		assertTrue(q4Level1_attr_1.isActive());
		
	}
	
	@Test
	public void t05_rule_deactivate_nested() {
		// verify initial state
		t00_init();
		
		Param<String> q4 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4);
		Param<?> q4Level1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level1);
		Param<?> q4Level2 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level2);
		Param<String> q4_attrib_1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4_nested2_attr_String_1);
		Param<String> q4_attrib_2 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4_nested2_attr_String_2);
		Param<?> q4Level1_attr_1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q4Level1_nested2_attr_String_1);
		
		final String state_q4_attrib_1 = "Y";
		final String state_q4_attrib_2 = "N";
		q4_attrib_1.setState(state_q4_attrib_1);
		q4_attrib_2.setState(state_q4_attrib_2);
		
		
		assertEquals(state_q4_attrib_1, q4_attrib_1.getState());
		assertEquals(state_q4_attrib_2, q4_attrib_2.getState());
		assertNull(q4Level1.getState());
		assertNull(q4Level2.getState());
		assertNull(q4Level1_attr_1.getState());
		
		assertTrue(q4.isActive());
		assertFalse(q4Level1.isActive());
		assertFalse(q4Level2.isActive());
		assertFalse(q4Level1_attr_1.isActive());
		
	}
}
