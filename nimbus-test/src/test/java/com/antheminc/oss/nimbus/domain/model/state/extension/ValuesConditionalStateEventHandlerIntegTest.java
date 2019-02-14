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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;

import java.util.Random;

import org.junit.Assert;
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
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreValuesEntity.Status;

/**
 * 
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValuesConditionalStateEventHandlerIntegTest extends AbstractStateEventHandlerTests{

	private static final String STATUS_FORM = "/sample_core/sampleCoreValuesEntity/statusForm";

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
		//AtomicInteger counter = new AtomicInteger(0);
		final SampleCoreEntity core = new SampleCoreEntity();
		core.setId(new Random().nextLong());
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
	public void t01_stateChange() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
		
		statusForm.findParamByPath("/statusReason").setState("B1");
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
		
		statusForm.findParamByPath("/statusReason").setState("A1");
		statusForm.findParamByPath("/status").setState("B");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t02_useDefaultsOnUnknownState() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		statusForm.findParamByPath("/status").setState("UNKNOWN");
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
	}
	
	@Test
	public void t03_allowOverridingCondition() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		statusForm.findParamByPath("/statusReason").setState("B1");
		statusForm.findParamByPath("/allowOverrideStatus").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t04_disallowOverridingCondition() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		statusForm.findParamByPath("/statusReason").setState("B1");
		statusForm.findParamByPath("/disallowOverrideStatus").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t05_noDefaultsDefined() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
		
		statusForm.findParamByPath("/status").setState("UNKNOWN");
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t06_multipleConfigs() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getState());
		
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		
		statusForm.findParamByPath("/status").setState(null);
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());;
	}
	
	@Test
	public void t07_resetOnChange_false_miss() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		
		statusForm.findParamByPath("/status").setState("B");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
	}
	
	@Test
	public void t08_resetOnChange_false_preservePreviouslySelectedState() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
		
		// select the state
		statusForm.findParamByPath("/statusReason").setState("B1");
		
		// update the values
		statusForm.findParamByPath("/status").setState("B");
		
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		
		// validate the state was preserved
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t09_resetOnChange_true_doReset() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getState());
		
		statusForm.findParamByPath("/statusReason2").setState("A1");
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason").getState());
	}
	
	@Test
	public void t10_resetOnChange_false_noValuesDefinedOnConfig() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getState());
		
		statusForm.findParamByPath("/status2").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		
		// select a found value
		statusForm.findParamByPath("/statusReason2").setState("A1");
		
		// change the values
		statusForm.findParamByPath("/status2").setState("B");
		
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getState());
	}
	
	@Test
	public void t11_nonStringState() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNotNull(statusForm.findParamByPath("/contactStatus").getValues());
		Assert.assertNull(statusForm.findParamByPath("/contactStatus").getState());
		
		statusForm.findParamByPath("/contactStatus").setState(Status.PAST);
		
		statusForm.findParamByPath("/contactType").setState("changeit");
		Assert.assertEquals(1, statusForm.findParamByPath("/contactStatus").getValues().size());
		Assert.assertEquals("PAST", statusForm.findParamByPath("/contactStatus").getValues().get(0).getCode());
		Assert.assertEquals("Past", statusForm.findParamByPath("/contactStatus").getValues().get(0).getLabel());
		
		Assert.assertEquals(Status.PAST, statusForm.findParamByPath("/contactStatus").getState());
	}
	
	@Test
	public void t12_arrayState() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNotNull(statusForm.findParamByPath("/contactStatus").getValues());
		Assert.assertNull(statusForm.findParamByPath("/contactStatus").getState());
		String[] defaultState = {"FUTURE"};
		statusForm.findParamByPath("/multiSelect").setState(defaultState);
		
		statusForm.findParamByPath("/contactType").setState("changeit");
		Assert.assertEquals(3, statusForm.findParamByPath("/multiSelect").getValues().size());
		Assert.assertEquals("FUTURE", statusForm.findParamByPath("/multiSelect").getValues().get(0).getCode());
		Assert.assertEquals("PAST", statusForm.findParamByPath("/multiSelect").getValues().get(1).getCode());
		Assert.assertEquals("CURRENT", statusForm.findParamByPath("/multiSelect").getValues().get(2).getCode());

		Assert.assertEquals(defaultState, statusForm.findParamByPath("/multiSelect").getState());
	}
}
