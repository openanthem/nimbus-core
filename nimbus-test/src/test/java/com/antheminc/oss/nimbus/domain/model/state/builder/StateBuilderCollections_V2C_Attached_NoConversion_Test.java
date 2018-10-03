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
package com.antheminc.oss.nimbus.domain.model.state.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateBuilderCollections_V2C_Attached_NoConversion_Test extends AbstractStateBuilderCollectionScenariosTest {

	private static final String CORE_PARAM_PATH = "/sample_core/attr_list_1_NestedEntity";
	private static final String VIEW_PARAM_PATH = "/sample_view/page_green/tile/list_attached_noConversion_NestedEntity";
	
	@SuppressWarnings("unchecked")
	protected ListParam<SampleCoreNestedEntity> coreListParam(QuadModel<?, ?> q) {
		return q.getRoot().findParamByPath(CORE_PARAM_PATH).findIfCollection();
	}
	
	@SuppressWarnings("unchecked")
	protected ListParam<SampleCoreNestedEntity> viewListParam(QuadModel<?, ?> q) {
		return q.getRoot().findParamByPath(VIEW_PARAM_PATH).findIfCollection();
	}

	protected List<SampleCoreNestedEntity> createCoreList(int size) {
		return createList("from core nested attr", size);
	}
	
	protected List<SampleCoreNestedEntity> createViewList(int size) {
		return createList("from view nested attr", size);
	}
	
	protected List<SampleCoreNestedEntity> createList(String prefix, int size) {
		List<SampleCoreNestedEntity> list = new ArrayList<>();
		
		for(int i=0; i<size; i++) {
			SampleCoreNestedEntity elem = new SampleCoreNestedEntity();
			elem.setNested_attr_String("@@ "+prefix+" : "+i);
			
			list.add(elem);
		}
		return list;
	}
	
	@Test @Override
	public void t0_init_check() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = buildQuad();
		assertNotNull(q);
		
		assertNotNull("Core Param not found: "+CORE_PARAM_PATH, q.getRoot().findParamByPath(CORE_PARAM_PATH));
		assertNotNull("View Param not found: "+VIEW_PARAM_PATH, q.getRoot().findParamByPath(VIEW_PARAM_PATH));
	}
	
	protected void runValidations(List<SampleCoreNestedEntity> vColEntity, QuadModel<?, ?> q) {
		// validate core
		int coreSize = coreListParam(q).size();
		assertEquals("Expected core size to be: "+vColEntity.size()+" but found: "+coreSize, vColEntity.size(), coreSize);
		
		for(int i=0; i<vColEntity.size(); i++) {
			assertSame(vColEntity.get(i).getNested_attr_String(), coreListParam(q).findParamByPath("/"+i+"/nested_attr_String").getState());
		}
		
		// validate view
		int viewSize = viewListParam(q).size();
		assertEquals("Expected view size to be: "+vColEntity.size()+" but found: "+viewSize, vColEntity.size(), viewSize);
		
		for(int i=0; i<vColEntity.size(); i++) {
			assertSame(vColEntity.get(i).getNested_attr_String(), viewListParam(q).findParamByPath("/"+i+"/nested_attr_String").getState());
		}
	}
	
	@Test @Override
	public void t1_new() {
		QuadModel<?, ?> q = buildQuad();
		List<SampleCoreNestedEntity> vColEntity = createViewList(30);
		
		final ListParam<SampleCoreNestedEntity> viewListParam = viewListParam(q);
		
		// Listener addressed in separate test case
		viewListParam.getAspectHandlers().setEventListener(null);
		
		// set
		Action a = viewListParam.setState(vColEntity);
		assertSame(Action._new, a);
		
		// validate
		runValidations(vColEntity, q);
	}
	
	@Test @Override
	public void t2_existing() {
		SampleCoreEntity core = new SampleCoreEntity();
		VRSampleViewRootEntity view = new VRSampleViewRootEntity();
		
		List<SampleCoreNestedEntity> cColEntity = createCoreList(30);
		core.setAttr_list_1_NestedEntity(cColEntity);
		
		QuadModel<?, ?> q = quadModelBuilder.build(createCommand(), ExecutionEntity.resolveAndInstantiate(view, core));
		q.getRoot().initState();
		assertNotNull(q);
		
		// validate: initial
		runValidations(cColEntity, q);
	}
	
	@Test @Override
	public void t3_existing_reset() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t4_existing_reset_set() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t5_existing_reset_add() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t6_existing_add() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t7_existing_addTwice() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t8_existing_addThrice() {
		// TODO Auto-generated method stub
		
	}
	
	@Test @Override
	public void t9_existing_addThrice_reset_addTwice() {
		// TODO Auto-generated method stub
		
	}
}
