/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedTransientParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateBuilderCollections_V2C_Attached_Transient_Test {

	private static final String CORE_PARAM_PATH = "/sample_core/attr_list_2_NestedEntity";

	private static final String VIEW_PARAM_MAPPED_PATH = "/sample_view/page_blue/tile/vm_attached_convertedList";
	private static final String VIEW_PARAM_TRANSIENT_PATH = "/sample_view/page_red/tile/vt_attached_convertedNestedEntity";

	@Autowired QuadModelBuilder quadModelBuilder;

	protected static Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	protected QuadModel<VRSampleViewRootEntity, SampleCoreEntity> buildQuad() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = quadModelBuilder.build(createCommand());
		return q;
	}
	
	@Test
	public void t0_init_check() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		assertNotNull(q);
		
		assertNotNull("Core Param not found: "+CORE_PARAM_PATH, q.getRoot().findParamByPath(CORE_PARAM_PATH));
		assertNotNull("View Mapped Collection Param not found: "+VIEW_PARAM_MAPPED_PATH, q.getRoot().findParamByPath(VIEW_PARAM_MAPPED_PATH));
		
		Param<?> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH);
		assertNotNull("View Mapped Transient Param not found: "+VIEW_PARAM_TRANSIENT_PATH, pTransient);
		assertTrue(pTransient.isTransient());
	}
	
	@Test
	public void t1_unassigned_init_validation() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		
		// check that param doesn't have any state
		Param<?> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH);
		assertNull(pTransient.getState());
		
		assertFalse(pTransient.findIfTransient().isAssinged());
	}
	
	@Test
	public void t2_unassigned_loadDb_validation() {
		// check that param doesn't have any state
	}
	
	@Test
	public void t3_assign_state_get() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		
		MappedTransientParam<?, ?> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH).findIfTransient();
		assertNull(pTransient.getState());
		
		// mapsTo collection core
		Param mapsToCol = q.getRoot().findParamByPath(CORE_PARAM_PATH);
		
		// assign core collection
		pTransient.assignMapsTo(mapsToCol);
		
		Param<?> pTransientMapsTo = pTransient.getMapsTo();
		assertNotNull(pTransientMapsTo);
		assertEquals(CORE_PARAM_PATH+"/0", pTransientMapsTo.getPath());
		
		// newly created transient element increments max index, but is not added to core list model
		assertTrue(mapsToCol.findIfCollection().getType().getModel().templateParams().isNullOrEmpty());
		
		assertNull(pTransient.getState());
		assertNull(pTransientMapsTo.getState());
	}
	
	@Test
	public void t4_assign_state_set_add_new() {
		
	}
	
	@Test
	public void t5_assign_state_set_add_existing() {
		
	}
	
	@Test
	public void t6_assign_state_set_edit() {
		
	}
	
	@Test
	public void t7_assign_state_set_delete() {
		
	}
	
	@Test
	public void t8_assign_state_set_add_unassign() {
		
	}
	
	@Test
	public void t9_assign_state_set_add_unassign_add() {
		
	}
	
	@Test
	public void t10_assign_state_set_add_unassign_repeat2() {
		
	}
	
	@Test
	public void t11_assign_state_set_add_unassign_repeat3() {
		
	}
	
	@Test
	public void t12_assign_state_set_edit_unassign_add_unassign() {
		
	}
	
	@Test
	public void t13_assign_state_set_edit_unassign_add_unassign_repeat2() {
		
	}
	
	@Test
	public void t14_assign_state_set_edit_unassign_add_unassign_repeat3() {
		
	}
}
