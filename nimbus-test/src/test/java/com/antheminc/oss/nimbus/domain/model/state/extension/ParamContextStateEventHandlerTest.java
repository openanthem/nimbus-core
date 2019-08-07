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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamContextStateEventHandlerTest extends AbstractStateEventHandlerTests {

	private Long REF_ID;
	
	@Override
	protected Command createCommand() {
		final Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view:"+REF_ID+"/_get").getCommand();
		return cmd;
	}

	private SampleCoreEntity createOrGetCore() {
		
		if (REF_ID != null) {
			return mongo.findById(REF_ID, SampleCoreEntity.class, "sample_core");
		}
		
		final SampleCoreEntity core = new SampleCoreEntity();
		core.setId(1L);
		mongo.insert(core, "sample_core");
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
	public void t1_setDefaultParamContext() {
		final Param<?> myModal = _q.getRoot().findParamByPath("/sample_core/for_set_param_context");
		assertNotNull(myModal);
		assertFalse(myModal.isVisible());
		assertFalse(myModal.isEnabled());
	}
	
	@Test
	public void t2_setDefaultParamContext_OverrideViewStyleDefaults() {
		final Param<?> myModal = _q.getRoot().findParamByPath("/sample_core/myModal1");
		assertNotNull(myModal);
		assertTrue(myModal.isVisible());
		assertTrue(myModal.isEnabled());
	}
}
