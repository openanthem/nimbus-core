/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedTransientParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;

import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreNestedEntity;
import test.com.anthem.oss.nimbus.core.domain.model.ui.VPSampleViewPageRed.Form_ConvertedNestedEntity;
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

	@MockBean protected ParamEventAMQPListener mockParamEventListener;
	
	protected static Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	protected QuadModel<VRSampleViewRootEntity, SampleCoreEntity> buildQuad() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = quadModelBuilder.build(createCommand());
		return q;
	}
	
	@Test
	public void t00_init_check() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		assertNotNull(q);
		
		assertNotNull("Core Param not found: "+CORE_PARAM_PATH, q.getRoot().findParamByPath(CORE_PARAM_PATH));
		assertNotNull("View Mapped Collection Param not found: "+VIEW_PARAM_MAPPED_PATH, q.getRoot().findParamByPath(VIEW_PARAM_MAPPED_PATH));
		
		Param<?> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH);
		assertNotNull("View Mapped Transient Param not found: "+VIEW_PARAM_TRANSIENT_PATH, pTransient);
		assertTrue(pTransient.isTransient());
	}
	
	@Test
	public void t01_unassigned_init_validation() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		
		// check that param doesn't have any state
		Param<?> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH);
		assertNull(pTransient.getState());
		
		assertFalse(pTransient.findIfTransient().isAssinged());
	}
	
	@Test
	public void t02_unassigned_loadDb_validation() {
		// check that param doesn't have any state
	}
	
	private Param<Form_ConvertedNestedEntity> findViewTransientParam(QuadModel<?, ?> q) {
		Param<Form_ConvertedNestedEntity> pTransient = q.getRoot().findParamByPath(VIEW_PARAM_TRANSIENT_PATH);	
		return pTransient;
	}
	
	private ListModel findCoreListModel(QuadModel<?, ?> q) {
		ListModel mapsToCol = q.getRoot().findModelByPath(CORE_PARAM_PATH).findIfListModel();
		return mapsToCol;
	}
	
	private QuadModel<?, ?> create_addFlow() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		
		MappedTransientParam<?, ?> pTransient = findViewTransientParam(q).findIfTransient();
		assertNull(pTransient.getState());
		
		// mapsTo collection core
		ListModel mapsToCol = findCoreListModel(q);
		
		// assign core collection
		pTransient.assignMapsTo(mapsToCol.getAssociatedParam());
		
		Param<?> pTransientMapsTo = pTransient.getMapsTo();
		assertTrue(pTransient.isAssinged());
		assertNotNull(pTransientMapsTo);
		assertEquals(CORE_PARAM_PATH+"/0", pTransientMapsTo.getPath());
		
		// newly created transient element increments max index, but is not added to core list model
		assertTrue(mapsToCol.templateParams().isNullOrEmpty());
		
		assertNull(pTransient.getState());
		assertNull(pTransientMapsTo.getState());
		
		return q;
	}
	
	@Test
	public void t03_assign_state_get() {
		create_addFlow();
	}
	
	@Test
	public void t04_assign_state_set_add_new() {
		QuadModel<?, ?> q = buildQuad();

		ListModel mapsToCol = findCoreListModel(q);
		Param<Form_ConvertedNestedEntity> pTransient = findViewTransientParam(q);

		// create new view element instance: simulate form submission
		final String K_VAL = "setting from form at: "+ new Date();
		
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL);
		
		// assign
		pTransient.findIfTransient().assignMapsTo(mapsToCol.getAssociatedParam());
		
		// set to form
		pTransient.setState(form);
		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertSame(K_VAL, pTransient.findParamByPath("/vt_nested_attr_String").getState());
	}
	
	@Test
	public void t05_assign_state_set_add_existing() {
		QuadModel<?, ?> q = buildQuad();

		ListModel mapsToCol = findCoreListModel(q);
		Param<Form_ConvertedNestedEntity> pTransient = findViewTransientParam(q);

		
		// create existing element directly in core
		final String K_VAL_EXISTING = "--existing-- at "+ new Date();
		SampleCoreNestedEntity existing = new SampleCoreNestedEntity();
		existing.setNested_attr_String(K_VAL_EXISTING);
		
		mapsToCol.add(existing);
		
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_EXISTING, mapsToCol.findParamByPath("/0/nested_attr_String").getState());

		// assign
		pTransient.findIfTransient().assignMapsTo(mapsToCol.getAssociatedParam());
		
		// create new view element instance: simulate form submission
		final String K_VAL = "setting from form at: "+ new Date();
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL);
		
		// set to form
		pTransient.setState(form);	
		
		assertEquals(2, mapsToCol.size());
		assertSame(K_VAL, mapsToCol.findParamByPath("/1/nested_attr_String").getState());
		assertSame(K_VAL, pTransient.findParamByPath("/vt_nested_attr_String").getState());
	}
	
	@Test
	public void t06_assign_state_set_edit_override() {
		QuadModel<?, ?> q = buildQuad();

		ListModel mapsToCol = findCoreListModel(q);
		Param<Form_ConvertedNestedEntity> pTransient = findViewTransientParam(q);

		// assign
		pTransient.findIfTransient().assignMapsTo(mapsToCol.getAssociatedParam());

		// create new view element instance: simulate form submission
		final String K_VAL_INITIAL = "setting initial at: "+ new Date();
		
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL_INITIAL);
		
		// set to form: initially
		pTransient.setState(form);
		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_INITIAL, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertSame(K_VAL_INITIAL, pTransient.findParamByPath("/vt_nested_attr_String").getState());

		
		// create new view element instance: simulate form submission
		final String K_VAL_NEXT = "setting next at: "+ new Date();
		
		Form_ConvertedNestedEntity form_next = new Form_ConvertedNestedEntity();
		form_next.setVt_nested_attr_String(K_VAL_NEXT);

		// set to form: next w/o doing a reassign
		pTransient.setState(form_next);
		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_NEXT, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertSame(K_VAL_NEXT, pTransient.findParamByPath("/vt_nested_attr_String").getState());

	}
	
	@Test
	public void t06_assign_state_set_edit_coreElem() {
		QuadModel<?, ?> q = buildQuad();

		ListModel mapsToCol = findCoreListModel(q);
		Param<Form_ConvertedNestedEntity> pTransient = findViewTransientParam(q);

		
		// create existing element directly in core
		final String K_VAL_EXISTING = "--existing-- at "+ new Date();
		SampleCoreNestedEntity existing = new SampleCoreNestedEntity();
		existing.setNested_attr_String(K_VAL_EXISTING);
		
		mapsToCol.add(existing);
		
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_EXISTING, mapsToCol.findParamByPath("/0/nested_attr_String").getState());

		// assign: existing core colElem
		pTransient.findIfTransient().assignMapsTo(mapsToCol.findParamByPath("/0"));
	
		// create new view element instance: simulate form submission
		final String K_VAL_NEXT = "setting next at: "+ new Date();
		
		Form_ConvertedNestedEntity form_next = new Form_ConvertedNestedEntity();
		form_next.setVt_nested_attr_String(K_VAL_NEXT);

		// set to form: next w/o doing a reassign
		pTransient.setState(form_next);
		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_NEXT, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertSame(K_VAL_NEXT, pTransient.findParamByPath("/vt_nested_attr_String").getState());

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
