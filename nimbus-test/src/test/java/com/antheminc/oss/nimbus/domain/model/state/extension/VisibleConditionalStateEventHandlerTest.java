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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Tony Lopez
 *
 */
public class VisibleConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}

	@Test
	public void t00_init() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_visible_trigger");
		assertNotNull(cp_trigger);
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested");
		assertNotNull(cp_action);
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		assertNotNull(cp_action_nested_p1);
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNotNull(cp_action_nested_p2);
	}
	
	@Test
	public void t01c_condition_NULL_onLoad() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_visible_trigger");
		assertNull(cp_trigger.getState());
		assertTrue(cp_trigger.isVisible());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested");
		assertNull(cp_action.getState());
		assertFalse(cp_action.isVisible());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		assertNull(cp_action_nested_p1.getState());
		assertFalse(cp_action_nested_p1.isVisible());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isVisible());
	}
	
	@Test
	public void t02c_condition_positive() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_visible_trigger");
		assertNull(cp_trigger.getState());
		
		// set positive scenario
		cp_trigger.setState("Y");
		assertTrue(cp_trigger.isVisible());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested");
		assertNull(cp_action.getState());
		assertTrue(cp_action.isVisible());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		assertNull(cp_action_nested_p1.getState());
		assertTrue(cp_action_nested_p1.isVisible());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isVisible());
	}
	
	@Test
	public void t02c_condition_positive_then_negative() {
		// set positive scenario
		t02c_condition_positive();
		
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_visible_trigger");
		
		// set negative scenario
		cp_trigger.setState("N");
		assertTrue(cp_trigger.isVisible());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested");
		assertNull(cp_action.getState());
		assertFalse(cp_action.isVisible());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		assertNull(cp_action_nested_p1.getState());
		assertFalse(cp_action_nested_p1.isVisible());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isVisible());
	}
	
	@Test
	public void t02c_condition_nested_positive() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_visible_trigger");
		assertNull(cp_trigger.getState());
		
		// set positive scenario
		cp_trigger.setState("Y");
		assertTrue(cp_trigger.isVisible());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested");
		assertNull(cp_action.getState());
		assertTrue(cp_action.isVisible());
		
		// set nested param trigger
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		cp_action_nested_p1.setState("show me");
		
		assertTrue(cp_action_nested_p1.isVisible());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNull(cp_action_nested_p2.getState());
		assertTrue(cp_action_nested_p2.isVisible());
	}
	
	@Test
	public void t02c_condition_nested_positive_then_negative() {
		// set positive scenario
		t02c_condition_nested_positive();
		
		// set negative scenario
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p1");
		cp_action_nested_p1.setState("dont show me");
		
		assertTrue(cp_action_nested_p1.isVisible());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_visible_nested/visible_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isVisible());
	}
}
