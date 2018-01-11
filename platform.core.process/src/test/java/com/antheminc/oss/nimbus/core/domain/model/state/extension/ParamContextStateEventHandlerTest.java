package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
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
public class ParamContextStateEventHandlerTest extends AbstractStateEventHandlerTests {

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
