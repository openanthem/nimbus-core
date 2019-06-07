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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.EntityConfigVisitor;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultEntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleExprEvalEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExpressionConditionalStateEventHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired 
	DefaultEntityConfigBuilder entityConfigBuilder;
	
	@Autowired 
	CommandPathVariableResolver cmdPathResolver;
	
	@Autowired
	QuadModelBuilder quadBuilder;
	
	ModelConfig<SampleExprEvalEntity> mConfig;
	
	@Value(SampleExprEvalEntity.K_LINK_URL)
	String expected_url_link;
	
	@Value(SampleExprEvalEntity.K_IMAGE_URL)
	String expected_url_image;
	
	@Value(SampleExprEvalEntity.K_INITIALIZE_URL)
	String expected_url_initialize;
	
	@Value("${test.url.code}")
	String expected_env_code;
	
	public static final String K_PARAM_REF_VALUE = "pass value p1";
	
	
	@Before
	public void before() {
		mConfig = entityConfigBuilder.load(SampleExprEvalEntity.class, new EntityConfigVisitor());
	}
	
	@Test
	public void t01_onLoad() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_expr/_new").getCommand();
		
		QuadModel<?, ?> q = quadBuilder.build(cmd);
		assertNotNull(q);
		
		Param<LocalDate> pInitOnLoad = q.getCore().findParamByPath("/initDateOnLoad");
		assertNotNull(pInitOnLoad.getState());
		assertEquals(LocalDate.now(), pInitOnLoad.getState());
		
		// set some value after onLoad
		LocalDate newDate = LocalDate.of(2000, 1, 1);
		pInitOnLoad.setState(newDate);
		assertEquals(newDate, pInitOnLoad.getState());
	}
	
	@Test
	public void t02_onChange() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_expr/_new").getCommand();
		
		QuadModel<?, ?> q = quadBuilder.build(cmd);
		assertNotNull(q);
		
		Param<String> trigger = q.getCore().findParamByPath("/exprWhenTrigger");
		Param<String> exprThen = q.getCore().findParamByPath("/exprThen");
		
		assertNotNull(trigger);
		assertNotNull(exprThen);
		
		assertNull(exprThen.getState());
		
		// thenB
		trigger.setState(SampleExprEvalEntity.K_VAL_B_WHEN);
		assertEquals(SampleExprEvalEntity.K_VAL_B_THEN, exprThen.getState());
		
		// not thenB
		trigger.setState("something else...");
		
		// check that old value is still retained
		assertEquals(SampleExprEvalEntity.K_VAL_B_THEN, exprThen.getState());
	}
}
