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
package com.antheminc.oss.nimbus.domain.model.state.builder;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedTransientParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageBlue.Section_ConvertedNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen.ConvertedNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageRed.Form_ConvertedNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrameworkIntegrationTestScenariosApplication.class)
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
	
	@Test
	public void t03_assign_state_get() {

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
		assertFalse(mapsToCol.templateParams().isNullOrEmpty());
		assertEquals(1, mapsToCol.templateParams().size());
		
		assertNull(pTransient.getState());
		assertNull(pTransientMapsTo.getState());
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
		
		// call flush
		pTransient.findIfTransient().flush();
		
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
		
		// call flush
		pTransient.findIfTransient().flush();
		
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

		// call flush
		pTransient.findIfTransient().flush();

		
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
		
		// call flush
		pTransient.findIfTransient().flush();

		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertEquals(K_VAL_NEXT, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertEquals(K_VAL_NEXT, pTransient.findParamByPath("/vt_nested_attr_String").getState());

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

		// call flush
		pTransient.findIfTransient().flush();

		
		// verify if element got added to core mapped collection
		assertEquals(1, mapsToCol.size());
		assertSame(K_VAL_NEXT, mapsToCol.findParamByPath("/0/nested_attr_String").getState());
		assertSame(K_VAL_NEXT, pTransient.findParamByPath("/vt_nested_attr_String").getState());

	}
	
	@Test
	public void t07_assign_addForm_addCore_assignEdit_updateExisting() {
		QuadModel<?, ?> q = buildQuad();

		ListModel mapsToCol = findCoreListModel(q);
		Param<Form_ConvertedNestedEntity> pTransient = findViewTransientParam(q);

		// assign
		pTransient.findIfTransient().assignMapsTo(mapsToCol.getAssociatedParam());

		// user submit form data 
		final String K_VAL_0 = "setting from form at: "+ new Date();
		Form_ConvertedNestedEntity form = new Form_ConvertedNestedEntity();
		form.setVt_nested_attr_String(K_VAL_0);
		
		pTransient.setState(form);

		// call flush
		pTransient.findIfTransient().flush();

		
		// add value to mapsTo core to see effect in mapped transient
		final String K_VAL_1 = "TEST_INTG_COL_ELEM_add "+ new Date();
		SampleCoreNestedEntity colElemState = new SampleCoreNestedEntity();
		colElemState.setNested_attr_String(K_VAL_1);
		
		mapsToCol.add(colElemState);
		
		// validate mapped view grid is updated with 2 records
		ListParam<Section_ConvertedNestedEntity> listGridConverted = q.getView().findParamByPath("/page_blue/tile/vm_attached_convertedList").findIfCollection();
		assertNotNull(listGridConverted);
		assertSame(2, listGridConverted.size());
		assertEquals(K_VAL_0, listGridConverted.getLeafState().get(0).getVm_nested_attr_String());
		assertEquals(K_VAL_1, listGridConverted.getLeafState().get(1).getVm_nested_attr_String());
		
		// user clicks on edit link in grid :: assign colElem for edit
		Param pCore_0 = listGridConverted.findParamByPath("/0/editButton").getParentModel().findIfMapped().getMapsTo().getAssociatedParam();
		assertNotNull(pCore_0);
		Param<?> mappedTransient = listGridConverted.findParamByPath("/0/editButton")
														.getRootDomain().findParamByPath("/page_red/tile/vt_attached_convertedNestedEntity");
		
		mappedTransient.findIfTransient().assignMapsTo(pCore_0);
		
		// user submits form to update existing 
		final String K_VAL_0_updated = "updating from form at: "+ new Date();
		form.setVt_nested_attr_String(K_VAL_0_updated);
		
		pTransient.setState(form);
		
		// call flush
		pTransient.findIfTransient().flush();

		
		// validate
		assertSame(2, listGridConverted.size());
		assertEquals(K_VAL_0_updated, listGridConverted.getLeafState().get(0).getVm_nested_attr_String());
		assertEquals(K_VAL_1, listGridConverted.getLeafState().get(1).getVm_nested_attr_String());
		
		assertSame(2, mapsToCol.size());
		SampleCoreEntity core = (SampleCoreEntity)q.getCore().getState();
		assertEquals(K_VAL_0_updated, core.getAttr_list_2_NestedEntity().get(0).getNested_attr_String());
		assertEquals(K_VAL_1, core.getAttr_list_2_NestedEntity().get(1).getNested_attr_String());
	}
	
	@Test
	public void t08_assign_state_set_delete() {
		QuadModel<?, ?> q = buildQuad();
		
		ListModel<SampleCoreNestedEntity> mapsToCol = findCoreListModel(q);
		Param pTransient = findViewTransientParam(q);
		
		// add value to mapsTo core
		final String K_VAL_0 = "0. TEST_INTG_COL_ELEM_add "+ new Date();
		SampleCoreNestedEntity elem0 = new SampleCoreNestedEntity();
		elem0.setNested_attr_String(K_VAL_0);
		mapsToCol.add(elem0);
		
		final String K_VAL_1 = "1. TEST_INTG_COL_ELEM_add "+ new Date();
		SampleCoreNestedEntity elem1 = new SampleCoreNestedEntity();
		elem1.setNested_attr_String(K_VAL_1);
		mapsToCol.add(elem1);
		
		// assign 0th elem to transient parameter
		pTransient.findIfTransient().assignMapsTo(mapsToCol.getParams().get(0));
		
		// simulate delete action
		boolean removed = pTransient.findIfMapped().getMapsTo().findIfCollectionElem().remove();
		assertTrue(removed);

		// unassign
		pTransient.findIfTransient().unassignMapsTo();
		assertFalse(pTransient.findIfTransient().isAssinged());
		
		// validate
		assertSame(1, mapsToCol.size());
		assertEquals(K_VAL_1, mapsToCol.findParamByPath("/1/nested_attr_String").getState());
		
		ListParam<ConvertedNestedEntity> listGridConverted = q.getView().findParamByPath("/page_green/tile/section_grid/grid_attached_ConvertedItems").findIfCollection();
		assertSame(1, listGridConverted.size());
		assertEquals(K_VAL_1, listGridConverted.findParamByPath("/1/nested_attr_String").getState());
		
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
