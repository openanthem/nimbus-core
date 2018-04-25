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
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActivateConditionalNoConversionTest extends AbstractStateEventHandlerTests {

	private static final String CORE_PARAM_nc_form = "/sample_core/nc_form";
	private static final String VIEW_PARAM_nc_form = "/sample_view/page_green/tile/view_sample_form/view_nc_form";

	private Long REF_ID;
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view:"+REF_ID+"/_get").getCommand();
		return cmd;
	}
	
	private SampleCoreEntity createOrGetCore() {
		if(REF_ID != null)
			return mongo.findById(REF_ID, SampleCoreEntity.class, "sample_core");
		
		SampleCoreEntity core = new SampleCoreEntity();
//		core.setNc_form(new SampleForm());
//		
//		core.getNc_form().setNc_nested0_Details(new SampleNoConversionEntity());
//		
//		core.getNc_form().getNc_nested0_Details().setNc_nested_level1(new NestedNoConversionLevel1());
		
//		core.getNc_form().getNc_nested0_Details().getNc_nested_level1()
//			.setNested_nc_attr1A("No");
		core.setId(1L);
		mongo.insert(core, "sample_core");
		
		REF_ID = core.getId();
		assertNotNull(REF_ID);
		
		return core;
	}
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		createOrGetCore();
		
		_cmd = createCommand();

		executionContextLoader.clear();
		
		_q = (QuadModel<?, ? extends IdLong>)executionContextLoader.load(_cmd).getQuadModel();
		assertNotNull(_q);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
	}
	
	@Test
	public void t00_init() {
		Param<?> view_nc_form = _q.getRoot().findParamByPath(VIEW_PARAM_nc_form);
		assertNotNull(view_nc_form);
		assertTrue(view_nc_form.isActive());
		
		Param<?> core_nc_form = _q.getRoot().findParamByPath(CORE_PARAM_nc_form);
		assertNotNull(core_nc_form);
		assertTrue(core_nc_form.isActive());
	}
	
	private static void checkIsActive(Param<?> p) {
		assertTrue(p.isActive());
		assertTrue(p.isVisible());
		assertTrue(p.isEnabled());
	}
	
	private static void checkIsInactive(Param<?> p) {
		assertFalse(p.isActive());
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t01_view_get_shell_entity() {
		Param<?> view_nc_form = _q.getRoot().findParamByPath(VIEW_PARAM_nc_form);
		assertNotNull(view_nc_form);
		
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details"));
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1"));
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nested_nc_attr1A"));
		
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2"));
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nested_nc_attr2C"));
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nc_nested_level3"));
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nc_nested_level3/nested_nc_attr3D"));
		
		// set
		view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nested_nc_attr1A")
			.setState("No");
		
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details"));
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1"));
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nested_nc_attr1A"));
		
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2"));
		checkIsActive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nested_nc_attr2C"));
		
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nc_nested_level3"));
		checkIsInactive(view_nc_form.findParamByPath("/nc_nested0_Details/nc_nested_level1/nc_nested_level2/nc_nested_level3/nested_nc_attr3D"));
		
	}
}		