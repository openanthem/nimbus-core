package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValuesConditionalStateEventHandlerIntegTest extends AbstractStateEventHandlerTests{

	private static final String STATUS_FORM = "/sample_core/statusForm";

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
		
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		
		statusForm.findParamByPath("/status").setState("B");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
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
		
		statusForm.findParamByPath("/allowOverrideStatus").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
	}
	
	@Test
	public void t04_disallowOverridingCondition() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		statusForm.findParamByPath("/disallowOverrideStatus").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
	}
	
	@Test
	public void t05_noDefaultsDefined() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		
		statusForm.findParamByPath("/status").setState("UNKNOWN");
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
	}
	
	@Test
	public void t06_multipleConfigs() {
		final Param<?> statusForm = _q.getRoot().findParamByPath(STATUS_FORM);
		assertNotNull(statusForm);
		
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
		
		statusForm.findParamByPath("/status").setState("A");
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals(1, statusForm.findParamByPath("/statusReason2").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason2").getValues().get(0).getCode());
		
		statusForm.findParamByPath("/status").setState(null);
		Assert.assertEquals(2, statusForm.findParamByPath("/statusReason").getValues().size());
		Assert.assertEquals("A1", statusForm.findParamByPath("/statusReason").getValues().get(0).getCode());
		Assert.assertEquals("B1", statusForm.findParamByPath("/statusReason").getValues().get(1).getCode());
		Assert.assertNull(statusForm.findParamByPath("/statusReason2").getValues());
	}
}
