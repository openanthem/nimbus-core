package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValuesConditionalStateEventHandlerIntegTest extends AbstractStateEventHandlerTests{

	private static final String STATUS_FORM = "/sample_core/sampleCoreValuesEntity/statusForm";

	private String REF_ID;
	
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
		
		_q = (QuadModel<?, ? extends IdString>)executionContextLoader.load(_cmd).getQuadModel();
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
}
