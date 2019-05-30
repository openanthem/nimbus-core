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
package com.antheminc.oss.nimbus.domain.bpm.activiti;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
