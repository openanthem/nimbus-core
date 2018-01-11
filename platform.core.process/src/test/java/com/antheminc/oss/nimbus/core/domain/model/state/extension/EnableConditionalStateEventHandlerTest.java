/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnableConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	@Test
	public void t00_init() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_enable_trigger");
		assertNotNull(cp_trigger);
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested");
		assertNotNull(cp_action);
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		assertNotNull(cp_action_nested_p1);
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNotNull(cp_action_nested_p2);
	}
	
	@Test
	public void t01c_condition_NULL_onLoad() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_enable_trigger");
		assertNull(cp_trigger.getState());
		assertTrue(cp_trigger.isEnabled());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested");
		assertNull(cp_action.getState());
		assertFalse(cp_action.isEnabled());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		assertNull(cp_action_nested_p1.getState());
		assertFalse(cp_action_nested_p1.isEnabled());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isEnabled());
	}
	
	@Test
	public void t02c_condition_positive() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_enable_trigger");
		assertNull(cp_trigger.getState());
		
		// set positive scenario
		cp_trigger.setState("Y");
		assertTrue(cp_trigger.isEnabled());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested");
		assertNull(cp_action.getState());
		assertTrue(cp_action.isEnabled());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		assertNull(cp_action_nested_p1.getState());
		assertTrue(cp_action_nested_p1.isEnabled());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isEnabled());
	}
	
	@Test
	public void t02c_condition_positive_then_negative() {
		// set positive scenario
		t02c_condition_positive();
		
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_enable_trigger");
		
		// set negative scenario
		cp_trigger.setState("N");
		assertTrue(cp_trigger.isEnabled());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested");
		assertNull(cp_action.getState());
		assertFalse(cp_action.isEnabled());
		
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		assertNull(cp_action_nested_p1.getState());
		assertFalse(cp_action_nested_p1.isEnabled());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isEnabled());
	}
	
	@Test
	public void t02c_condition_nested_positive() {
		Param<String> cp_trigger = _q.getRoot().findParamByPath("/sample_core/attr_enable_trigger");
		assertNull(cp_trigger.getState());
		
		// set positive scenario
		cp_trigger.setState("Y");
		assertTrue(cp_trigger.isEnabled());
		
		Param<String> cp_action = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested");
		assertNull(cp_action.getState());
		assertTrue(cp_action.isEnabled());
		
		// set nested param trigger
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		cp_action_nested_p1.setState("Joker");
		
		assertTrue(cp_action_nested_p1.isEnabled());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNull(cp_action_nested_p2.getState());
		assertTrue(cp_action_nested_p2.isEnabled());
	}
	
	@Test
	public void t02c_condition_nested_positive_then_negative() {
		// set positive scenario
		t02c_condition_nested_positive();
		
		// set negative scenario
		Param<String> cp_action_nested_p1 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p1");
		cp_action_nested_p1.setState("Batman");
		
		assertTrue(cp_action_nested_p1.isEnabled());
		
		Param<String> cp_action_nested_p2 = _q.getRoot().findParamByPath("/sample_core/attr_enable_nested/enable_p2");
		assertNull(cp_action_nested_p2.getState());
		assertFalse(cp_action_nested_p2.isEnabled());
	}
}
