/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

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
public class ActivateParamRuleTest  extends ActivateParamBaseTest {
	
	private static final String CORE_PARAM_PATH_q2 = "/sample_core/q2";
	private static final String CORE_PARAM_PATH_q2Level1 = "/sample_core/q2Level1";
	private static final String CORE_PARAM_PATH_q2Level1_nested_attr_String = CORE_PARAM_PATH_q2Level1 + "/nested_attr_String";

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
}
