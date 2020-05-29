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

import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

/**
 * 
 * @author Tony Lopez
 *
 */
public class RuleStateEventHandlerTest extends AbstractStateEventHandlerTests {

	public static final String ENTITY_CORE = "sample_core";
	public static final String ENTITY_VIEW = "sample_view";
	public static final String ENTITY_BASEPATH = "/" + ENTITY_CORE;
	
	private Long REF_ID;
	
	@Override
	protected Command createCommand() {
		final Command cmd = CommandBuilder.withUri("/hooli/thebox/p/" + ENTITY_VIEW + ":"+REF_ID+"/_get").getCommand();
		return cmd;
	}

	private SampleCoreEntity createOrGetCore() {
		
		if (REF_ID != null) {
			return mongo.findById(REF_ID, SampleCoreEntity.class, ENTITY_CORE);
		}
		
		final SampleCoreEntity core = new SampleCoreEntity();
		core.setId(new Random().nextLong());
		mongo.insert(core, ENTITY_CORE);
		REF_ID = core.getId();
		assertNotNull(REF_ID);
		
		return core;
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		this.createOrGetCore();
		
		_cmd = createCommand();

		executionContextLoader.clear();
		
		_q = (QuadModel<?, ? extends IdLong>)executionContextLoader.load(_cmd).getQuadModel();
		assertNotNull(_q);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
	}
	
	@Test
	public void t01_stateChange() {
		final Param<String> ruleParam = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param");
		final Param<Integer> ruleParam_affectState = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param_affectState");
		
		assertNotNull(ruleParam);
		assertNotNull(ruleParam_affectState);
		Assert.assertEquals(2, (int) ruleParam_affectState.getState());
		
		ruleParam.setState("Hello");
		Assert.assertEquals(3, (int) ruleParam_affectState.getState());
	}
	
	@Test
	public void t02_stateChange_cachedRuleConfig() {
		final Param<String> ruleParam = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param");
		final Param<String> ruleParam2 = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param2");
		final Param<Integer> ruleParam_affectState = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param_affectState");
		
		assertNotNull(ruleParam);
		assertNotNull(ruleParam_affectState);
		Assert.assertEquals(2, (int) ruleParam_affectState.getState());
		
		ruleParam.setState("Hello");
		Assert.assertEquals(3, (int) ruleParam_affectState.getState());
		
		ruleParam2.setState("World");
		Assert.assertEquals(4, (int) ruleParam_affectState.getState());
	}
	
	@Test
	public void t03_stateChange_DecisionTable() {
		final Param<String> dtableParam = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/dtable_param");
		final Param<String> dtableParam_affectState = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/dtable_param_affectState");
		
		assertNotNull(dtableParam);
		assertNotNull(dtableParam_affectState);
		
		dtableParam.setState("Red");
		Assert.assertEquals("Red Page", dtableParam_affectState.getState());	
	}
}
