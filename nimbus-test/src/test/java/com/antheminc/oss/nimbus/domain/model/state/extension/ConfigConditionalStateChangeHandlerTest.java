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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.audit.AuditEntry;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreAssociatedEntity;


/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigConditionalStateChangeHandlerTest extends AbstractStateEventHandlerTests {

	private static final String CORE_CONDITIONAL_PARAM_PATH = "/sample_core/conditional_config_attr";
	private static final String CORE_CONDITIONALS_PARAM_PATH = "/sample_core/conditionals_config_attr";
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	@Override
	public void before() {
		super.before();
		
		// ensure db has no existing records for @Config - action related entries
		assertFalse(mt.collectionExists("samepl_core_audit_history"));
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}
	
	@Test
	public void t00_init_check() {
		assertNotNull(_q.getRoot().findParamByPath(CORE_CONDITIONAL_PARAM_PATH));
		assertNotNull(_q.getRoot().findParamByPath(CORE_CONDITIONALS_PARAM_PATH));
	}
	
	@Test
	public void t01c_condition_Y_exec() {
		Param<String> cp = _q.getRoot().findParamByPath(CORE_CONDITIONAL_PARAM_PATH);
		
		cp.setState("Y");
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(_q.getCore().getState().getId(), audit.get(0).getDomainRootRefId());
		
	}
	
	@Test 
	public void t02c_condition_N_no_exec() {
		Param<String> cp = _q.getRoot().findParamByPath(CORE_CONDITIONAL_PARAM_PATH);
		
		cp.setState("N");
		
		assertFalse(mt.collectionExists("samepl_core_audit_history"));
	}
	
	@Test
	public void t03c_any_change_exec() {
		ListParam<String> cp = _q.getRoot().findParamByPath("/sample_core/conditional_config_attr_list_String").findIfCollection();
		assertNotNull(cp);
		assertNull(cp.getState());
		assertFalse(mt.collectionExists("samepl_core_audit_history"));
		
		// set
		final String K_elem1 = "1. elem @ "+ new Date();
		cp.add(K_elem1);
		
		// entity validate
		assertNotNull(cp.getState());
		assertTrue(CollectionUtils.contains(cp.getState().iterator(), K_elem1));
		
		// db validate
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(_q.getCore().getState().getId(), audit.get(0).getDomainRootRefId());
	}
	
	@Test
	public void t04c_no_change_no_exec() {
		
	}
	
	@Test
	public void t05c_on_db_load_no_exec() {
		
	}
	
	@Test
	public void t06c_multiple_execs() {
		
	}
	
	@Test
	public void t07c_multiple_annotations() {
		
	}
	
	// ---- REPEAT for view -----
	@Test
	public void t01v_condition_Y_exec() {
		Param<String> cp = _q.getRoot().findParamByPath("/sample_core/for_mapped_state_change_attr");
		Param<String> vp = _q.getRoot().findParamByPath("/sample_view/page_green/tile/for_mapped_state_change_attr");
		
		assertNotNull(cp);
		assertNotNull(vp);
		assertNull(cp.getState());
		assertNull(vp.getState());
		
		// set core
		final String K_val = "Y";
		cp.setState(K_val);
		assertSame(K_val, cp.getState());
		assertSame(K_val, vp.getState());
		
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_view_audit_history");
		assertEquals(1, audit.size());
		assertEquals(_q.getCore().getState().getId(), audit.get(0).getDomainRootRefId());
	}
	
	@Test
	public void t28_detached_domain_root() {
		// test with value from main domain root
	}
	
	@Test
	public void t01_config_conditionals_collections_exist_test() {
		Param<String> cp = _q.getRoot().findParamByPath(CORE_CONDITIONALS_PARAM_PATH);

		cp.setState("Y");
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(_q.getCore().getState().getId(), audit.get(0).getDomainRootRefId());

		cp.setState("N");
		List<SampleCoreAssociatedEntity> associatedEntity = mongo.findAll(SampleCoreAssociatedEntity.class, "sample_coreassociatedentity");
		assertEquals(1, associatedEntity.size());
		assertEquals(_q.getCore().getState().getId(), associatedEntity.get(0).getEntityId());
	}

	@Test
	public void t02_config_conditionals_collections_does_not_exist_test() {
		Param<String> cp = _q.getRoot().findParamByPath(CORE_CONDITIONALS_PARAM_PATH);

		assertFalse(mt.collectionExists("sample_core_audit_history"));
		cp.setState("Y");
		assertTrue(mt.collectionExists("sample_core_audit_history"));
		
		assertFalse(mt.collectionExists("sample_coreassociatedentity"));
		cp.setState("N");
		assertTrue(mt.collectionExists("sample_core_audit_history"));
		assertTrue(mt.collectionExists("sample_coreassociatedentity"));
	}

}
