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
package com.antheminc.oss.nimbus.test.scenarios.setbyrule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.rules.DecisionTableTestCoreModel;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.setbyrule.core.SampleSetByRuleCore;

/**
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SetByRuleDecisionTableTests extends AbstractFrameworkIngerationPersistableTests {

	public static final String ENTITY_CORE_ALIAS = "sample_setbyrule_core";
	protected static final String CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + ENTITY_CORE_ALIAS;
	private static final String ASSOCIATED_PARAM_CORE = "decisiontabletestcoremodel";
	private Long REF_ID;
	private Long ASSOCIATED_PARAM_ID;
	
	@Before
	public void setup() {		
		final DecisionTableTestCoreModel core = new DecisionTableTestCoreModel();
		core.setId(new Random().nextLong());
		core.setStateCheckParameter("Complete");
		mongo.insert(core, ASSOCIATED_PARAM_CORE);
		ASSOCIATED_PARAM_ID = core.getId();
		assertNotNull(ASSOCIATED_PARAM_ID);		
	}
	
	private SampleSetByRuleCore createOrGetCore() {
		
		if (REF_ID != null) {
			return mongo.findById(REF_ID, SampleSetByRuleCore.class, ENTITY_CORE_ALIAS);
		}
		
		final SampleSetByRuleCore core = new SampleSetByRuleCore();
		core.setId(new Random().nextLong());
		core.setAssociatedParamId(ASSOCIATED_PARAM_ID);
		mongo.insert(core, ENTITY_CORE_ALIAS);
		REF_ID = core.getId();
		assertNotNull(REF_ID);
		
		return core;
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t0_setByRule_associatedParam() {
		createOrGetCore();
		String updateUri = CORE_PARAM_ROOT + ":"+REF_ID+"/attr1";
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(updateUri)
				.addAction(Action._update)
				.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, converter.toJson("Start"));
		assertNotNull(holder);
		
		MockHttpServletRequest get_req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT + ":"+REF_ID+"/attr2")
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handleGet(get_req, null);
		
		MockHttpServletRequest get_req3 = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT + ":"+REF_ID+"/attr3")
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handleGet(get_req3, null);
		Param<String> attr3 = ExtractResponseOutputUtils.extractOutput(holder);
		assertEquals("InProgress",attr3.getLeafState());
		
		MockHttpServletRequest get_req4 = MockHttpRequestBuilder.withUri("/hooli/thebox/p/"+ASSOCIATED_PARAM_CORE + ":"+ASSOCIATED_PARAM_ID+"/triggeredParameter")
				.addAction(Action._get)
				.getMock();
		holder = (Holder<MultiOutput>)controller.handleGet(get_req4, null);
		Param<String> triggerParam = ExtractResponseOutputUtils.extractOutput(holder);
		assertEquals("Success",triggerParam.getLeafState());
	}
}
