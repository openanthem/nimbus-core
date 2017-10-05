/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActivateParamActivateDeactivateTest extends ActivateParamBaseTest {

	private static final String CORE_PARAM_PATH_q1 = "/sample_core/q1";
	private static final String CORE_PARAM_PATH_q1Level1 = "/sample_core/q1Level1";
	private static final String CORE_PARAM_PATH_q1Level1_nested_attr_String = CORE_PARAM_PATH_q1Level1 + "/nested_attr_String";

	@Override
	protected String getSourceParamPath() {
	 	return CORE_PARAM_PATH_q1;
	}
	
	@Override
	protected String getTargetParamPath() {
		return CORE_PARAM_PATH_q1Level1;
	}
	
	@Test
	public void t00_leaf_init() {
		Param<?> q1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1);
		assertTrue(q1.isActive());
		
		assertNull(q1.getState());
	}
	
	@Test
	public void t01_leaf_deactivate_noState() {
		Param<?> q1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1);
		assertTrue(q1.isActive());
		
		Param<Boolean> q1_visible = q1.findParamByPath("/#/visible");
		Param<Boolean> q1_enabled = q1.findParamByPath("/#/enabled");
		
		assertTrue(q1_visible.getState());
		assertTrue(q1_enabled.getState());
		
		addListener();
		q1.deactivate();
		
		assertFalse(q1.isActive());
		assertFalse(q1_visible.getState());
		assertFalse(q1_enabled.getState());
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(2, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(q1_visible);
		expectedEventParams.add(q1_enabled);
		
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
	}
	
	@Test
	public void t02_leaf_reactivate_noState() {
		// deactivate
		t01_leaf_deactivate_noState();
		_q.getRoot().getExecutionRuntime().onStopCommandExecution(_cmd);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
		activate_validate_leaf();
	}
	
	private void activate_validate_leaf() {
		Param<?> q1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1);
		assertFalse(q1.isActive());
		
		Param<Boolean> q1_visible = q1.findParamByPath("/#/visible");
		Param<Boolean> q1_enabled = q1.findParamByPath("/#/enabled");
		
		q1.activate();
		
		assertTrue(q1.isActive());
		assertTrue(q1_visible.getState());
		assertTrue(q1_enabled.getState());
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(2, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(q1_visible);
		expectedEventParams.add(q1_enabled);
		
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
	}
	
	@Test
	public void t03_leaf_deactivate_withState() {
		Param<String> q1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1);
		assertTrue(q1.isActive());
		
		Param<Boolean> q1_visible = q1.findParamByPath("/#/visible");
		Param<Boolean> q1_enabled = q1.findParamByPath("/#/enabled");
		
		assertTrue(q1_visible.getState());
		assertTrue(q1_enabled.getState());
		
		final String K_string = "some text @ "+ new Date();
		q1.setState(K_string);
		
		addListener();
		q1.deactivate();
		
		assertFalse(q1.isActive());
		assertFalse(q1_visible.getState());
		assertFalse(q1_enabled.getState());
		assertNull(q1.getState());
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(3, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(q1_visible);
		expectedEventParams.add(q1_enabled);
		expectedEventParams.add(q1);
		
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
	}
	
	@Test
	public void t04_leaf_reactivate_withState() {
		// deactivate 
		t03_leaf_deactivate_withState();
		_q.getRoot().getExecutionRuntime().onStopCommandExecution(_cmd);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
		activate_validate_leaf();
	}
	
	@Test
	public void t10_nested_init() {
		Param<?> q1Level1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1);
		assertTrue(q1Level1.isActive());
		
		assertNull(q1Level1.getState());
		
		assertTrue(_q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1_nested_attr_String).isActive());
	}
	
	@Test
	public void t11_nested_deactivate_noState() {
		Param<?> q1Level1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1);
		assertTrue(q1Level1.isActive());
		
		Param<Boolean> q1Level1_visible = q1Level1.findParamByPath("/#/visible");
		Param<Boolean> q1Level1_enabled = q1Level1.findParamByPath("/#/enabled");

		Param<String> q1Level1Attr = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1_nested_attr_String);
		assertTrue(q1Level1Attr.isActive());
		
		Param<Boolean> q1Level1Attr_visible = q1Level1Attr.findParamByPath("/#/visible");
		Param<Boolean> q1Level1Attr_enabled = q1Level1Attr.findParamByPath("/#/enabled");
		
		
		assertTrue(q1Level1_visible.getState());
		assertTrue(q1Level1_enabled.getState());
		
		assertTrue(q1Level1Attr_visible.getState());
		assertTrue(q1Level1Attr_enabled.getState());
		
		// check for nested param reset
		q1Level1Attr.setState("new nested @ "+new Date());

		
		addListener();
		q1Level1.deactivate();
		
		assertNull(q1Level1.getState());
		assertFalse(q1Level1.isActive());
		assertFalse(q1Level1_visible.getState());
		assertFalse(q1Level1_enabled.getState());
		
		assertNull(q1Level1Attr.getState());
		assertFalse(q1Level1Attr.isActive());
		assertFalse(q1Level1Attr_visible.getState());
		assertFalse(q1Level1Attr_enabled.getState());
		
		
		// validate events
		assertNotNull(_paramEvents);
		assertEquals(6, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(q1Level1_visible);
		expectedEventParams.add(q1Level1_enabled);
		expectedEventParams.add(q1Level1Attr_visible);
		expectedEventParams.add(q1Level1Attr_enabled);
		
		expectedEventParams.add(q1Level1);
		expectedEventParams.add(q1Level1Attr);
		
		
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
	}
	
	
	@Test
	public void t12_nested_reactivate_noState() {
		// deactivate
		t11_nested_deactivate_noState();
		_q.getRoot().getExecutionRuntime().onStopCommandExecution(_cmd);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
		activate_validate_nested();
	}
	
	private void activate_validate_nested() {
		Param<?> q1Level1 = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1);
		assertFalse(q1Level1.isActive());
		
		Param<Boolean> q1_visible = q1Level1.findParamByPath("/#/visible");
		Param<Boolean> q1_enabled = q1Level1.findParamByPath("/#/enabled");
		
		Param<?> q1Level1Attr = _q.getRoot().findParamByPath(CORE_PARAM_PATH_q1Level1_nested_attr_String);
		assertFalse(q1Level1Attr.isActive());
		
		Param<Boolean> q1Level1Attr_visible = q1Level1Attr.findParamByPath("/#/visible");
		Param<Boolean> q1Level1Attr_enabled = q1Level1Attr.findParamByPath("/#/enabled");
		
		
		q1Level1.activate();
		
		assertTrue(q1Level1.isActive());
		assertTrue(q1_visible.getState());
		assertTrue(q1_enabled.getState());

		assertTrue(q1Level1Attr.isActive());
		assertTrue(q1Level1Attr_visible.getState());
		assertTrue(q1Level1Attr_enabled.getState());
		
		// validate events
		assertNotNull(_paramEvents);
		//assertEquals(2, _paramEvents.size());
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(q1_visible);
		expectedEventParams.add(q1_enabled);
		
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
	}
	
}
