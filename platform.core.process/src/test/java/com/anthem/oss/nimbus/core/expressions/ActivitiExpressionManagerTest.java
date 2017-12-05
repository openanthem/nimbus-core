package com.antheminc.oss.nimbus.core.expressions;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.antheminc.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActivitiExpressionManagerTest {
	
	ActivitiExpressionManager expMgr;
	
	@Before
	public void setUp() {
		expMgr = new ActivitiExpressionManager();
	}
	
	@Test
	public void t01_evaluate_noArgs() {
		String expression = expMgr.evaluate("${'/p/cmcaseview/_new?fn=_initEntity&target=/.m/patientReferred&json='}${<!_json(model.getState())!>}");
		assertNotNull(expression);
	}
	
	@Test
	public void t01_evaluate_urlBuilder() {
		String expression = expMgr.evaluate("${<!_urlbuilder('/p/cmcaseview/_new','fn','_initEntity','target','/.m/patientReferred','json',_json(processContext.model.getState()))!>}");
		assertNotNull(expression);
	}	
	
	@Test
	public void t01_evaluate_array() {
		String expression = expMgr.evaluate("${<!_array(_urlbuilder('/p/cmcaseview/_new','fn','_initEntity','target','/.m/patientReferred','json',_json(processContext.model.getState())),_concat('/p/cmcaseview:',_getState(processContext.output.singleResult.findParamByPath(\"/.m/id\")),'/_nav?pageId=pageCaseInfo'))!>}");
		assertNotNull(expression);
	}		
	
}
