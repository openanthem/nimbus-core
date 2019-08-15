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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.drools.core.util.ArrayUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.defn.extension.Audit;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.audit.AuditEntry;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity.ComplexObject;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VPSampleViewPageGreen;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditStateChangeHandlerTest extends AbstractStateEventHandlerTests {

	private static final String CORE_audit_String = "/sample_core/audit_String";
	private static final String CORE_audit_Integer = "/sample_core/audit_Integer";
	private static final String CORE_unmapped_String = "/sample_core/unmapped_String";

	private static final String VIEW_audit_String = "/sample_view/page_green/tile/audit_String";
	private static final String VIEW_audit_Integer = "/sample_view/page_green/tile/audit_Integer";
	private static final String VIEW_unmapped_String = "/sample_view/page_green/tile/unmapped_String";
	
	private static final String CORE_audit_nested_attr = "/sample_core/level1/audit_nested_attr";
	private static final String VIEW_audit_nested_attr = "/sample_view/page_green/tile/level1/audit_nested_attr";
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	private Param<String> getCore_audit_string() {
		return _q.getRoot().findParamByPath(CORE_audit_String);
	}
	
	private Param<Integer> getCore_audit_integer() {
		return _q.getRoot().findParamByPath(CORE_audit_Integer);
	}
	
	private Param<String> getCore_unmapped_string() {
		return _q.getRoot().findParamByPath(CORE_unmapped_String);
	}
	
	
	private Param<String> getView_audit_string() {
		return _q.getRoot().findParamByPath(VIEW_audit_String);
	}
	
	private Param<Integer> getView_audit_integer() {
		return _q.getRoot().findParamByPath(VIEW_audit_Integer);
	}
	
	private Param<String> getView_unmapped_string() {
		return _q.getRoot().findParamByPath(VIEW_unmapped_String);
	}
	
	@Test
	public void t00_init_check() {
		assertNotNull(getCore_audit_string());
		assertNotNull(getCore_audit_integer());
		assertNotNull(getCore_unmapped_string());
		
		assertNotNull(getView_audit_string());
		assertNotNull(getView_audit_integer());
		assertNotNull(getView_unmapped_string());
		
		assertNotNull(getCore_audit_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "audit_String", true).getAnnotation(Audit.class)));
		assertNotNull(getCore_audit_integer().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "audit_Integer", true).getAnnotation(Audit.class)));
		assertNotNull(getCore_unmapped_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(SampleCoreEntity.class, "unmapped_String", true).getAnnotation(Audit.class)));
		assertTrue(CollectionUtils.isEmpty(getCore_unmapped_string().getEventSubscribers()));
		
		assertNotNull(getView_audit_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(VPSampleViewPageGreen.TileGreen.class, "audit_String", true).getAnnotation(Audit.class)));
		assertNull(getView_audit_integer().getConfig().getEventHandlerConfig());
		assertNotNull(getView_unmapped_string().getConfig().getEventHandlerConfig().findOnStateChangeHandler(FieldUtils.getField(VPSampleViewPageGreen.TileGreen.class, "unmapped_String", true).getAnnotation(Audit.class)));
		assertTrue(CollectionUtils.isEmpty(getView_unmapped_string().getEventSubscribers()));
	}
	
	@Test
	public void t01_core_string() {
		Param<String> p = getCore_audit_string();
		mapped_audit_string(p, "sample_core", "/sample_core/audit_String");
	}
	
	@Test
	public void t02_view_string() {
		Param<String> p = getView_audit_string();
		mapped_audit_string(p, "sample_core", "/sample_core/audit_String"); // core overrides when  both view and core are mapped with @Audit
	}
	
	private void mapped_audit_string(Param<String> p, String domainRootAlias, String propertyPath) {
		assertNull(p.getState());

		// change value #1
		final String K_state_1 = "1. new value @ "+new Date();
		p.setState(K_state_1);
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull(audit.get(0).getPreviousValue());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertEquals("string", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final String K_state_2 = "2. new value @ "+new Date();
		p.setState(K_state_2);
		
		audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals(domainRootAlias, audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull(audit.get(0).getPreviousValue());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals(domainRootAlias, audit.get(1).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(1).getDomainRootRefId());
		assertEquals(K_state_1, audit.get(1).getPreviousValue());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(propertyPath, audit.get(1).getPropertyPath());
		assertEquals("string", audit.get(1).getPropertyType());
		assertNotNull(audit.get(1).getCreatedDate());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}
	
	
	@Test
	public void t03_core_integer() {
		Param<Integer> p = getCore_audit_integer();
		mapped_audit_integer(p, "sample_core", "/sample_core/audit_Integer");
	}
	
	@Test
	public void t04_view_integer() {
		Param<Integer> p = getView_audit_integer();
		mapped_audit_integer(p, "sample_core", "/sample_core/audit_Integer"); // only core is annotated with @Audit while the param is mapped in view
	}
	
	private void mapped_audit_integer(Param<Integer> p, String domainRootAlias, String propertyPath) {
		assertNull(p.getState());

		// change value #1
		final Integer K_state_1 = 1;
		p.setState(K_state_1);
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		
		assertEquals(domainRootAlias, audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertEquals("integer", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final Integer K_state_2 = 2;
		p.setState(K_state_2);
		
		audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals(domainRootAlias, audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertEquals("integer", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals(domainRootAlias, audit.get(1).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(1).getDomainRootRefId());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(propertyPath, audit.get(1).getPropertyPath());
		assertEquals("integer", audit.get(1).getPropertyType());
		assertNotNull(audit.get(1).getCreatedDate());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}
	
	
	@Test
	public void t05_coreOnly_unmapped_String() {
		Param<String> p = getCore_unmapped_string();
		mapped_audit_string(p, "sample_core", "/sample_core/unmapped_String");
	}

	
	@Test
	public void t06_viewOnly_unmapped_String() {
		Param<String> p = getView_unmapped_string();
		String propertyPath = "/sample_view/page_green/tile/unmapped_String";
	
		assertNull(p.getState());

		// change value #1
		final String K_state_1 = "1. new value @ "+new Date();
		p.setState(K_state_1);
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_view_audit_history");
		assertEquals(1, audit.size());
		
		assertEquals("sample_view", audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull(audit.get(0).getPreviousValue());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertEquals("string", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final String K_state_2 = "2. new value @ "+new Date();
		p.setState(K_state_2);
		
		audit = mongo.findAll(AuditEntry.class, "sample_view_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals("sample_view", audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull(audit.get(0).getPreviousValue());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(propertyPath, audit.get(0).getPropertyPath());
		assertEquals("string", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals("sample_view", audit.get(1).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(1).getDomainRootRefId());
		assertEquals(K_state_1, audit.get(1).getPreviousValue());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(propertyPath, audit.get(1).getPropertyPath());
		assertEquals("string", audit.get(1).getPropertyType());
		assertNotNull(audit.get(1).getCreatedDate());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_core_audit_history"));
	}
	
	@Test
	public void t07_view_noConversion() {
		Param<String> cp = _q.getRoot().findParamByPath(CORE_audit_nested_attr);
		Param<String> vp = _q.getRoot().findParamByPath(VIEW_audit_nested_attr);
		
		assertNotNull(cp);
		assertNull(cp.getState());
		
		assertNotNull(vp);
		assertNull(vp.getState());
		
		// change value #1 in view
		final String K_state_1 = "1. new value @ "+new Date();
		vp.setState(K_state_1);
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		assertSame(K_state_1, vp.getState());
		assertSame(K_state_1, cp.getState());
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		
		assertEquals("sample_core", audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull(audit.get(0).getPreviousValue());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals("/sample_core/level1/audit_nested_attr", audit.get(0).getPropertyPath());
		assertEquals("string", audit.get(0).getPropertyType());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}

	@Test
	public void t08_core_string_array() {
		Param<String[]> cp = _q.getRoot().findParamByPath("/sample_core/attr_array_String");
		assertNotNull(cp);
		assertNull(cp.getState());
		
		assertTrue(cp.isLeaf());
		
		assertNotNull(cp.getConfig().getEventHandlerConfig().findOnStateChangeHandler(
				FieldUtils.getField(SampleCoreEntity.class, "attr_array_String", true).getAnnotation(Audit.class)));
		
		
		// change value
		final String K_arr[] = new String[]{"A", "B", "C @"+new Date()};
		cp.setState(K_arr);
		assertSame(K_arr, cp.getState());
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		
		assertEquals("sample_core", audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		
		// mongo stores the array in JSON and resurrects back as ArrayList. 
		// Non-consequential for consumers interacting via JSON, such as UI-client or WS-client  
		assertTrue(ArrayUtils.equals(K_arr, ((List<String>)audit.get(0).getNewValue()).toArray()));
		
		assertEquals("/sample_core/attr_array_String", audit.get(0).getPropertyPath());
		assertEquals("array-string", audit.get(0).getPropertyType());
	}
	
	@Test
	public void t09_core_string_list() {

		Param<List<String>> cp = _q.getRoot().findParamByPath("/sample_core/attr_list_String");
		assertNotNull(cp);
		assertNull(cp.getState());
		
		assertTrue(cp.isLeafOrCollectionWithLeafElems());
		
		assertNotNull(cp.getConfig().getEventHandlerConfig().findOnStateChangeHandler(
				FieldUtils.getField(SampleCoreEntity.class, "attr_list_String", true).getAnnotation(Audit.class)));
		
		
		// change value
		final List<String> K_list = Arrays.asList(new String[]{"A", "B", "C @"+new Date()});
		cp.setState(K_list);
		assertTrue(CollectionUtils.isEqualCollection(K_list, cp.getState()));
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		
		assertEquals("sample_core", audit.get(0).getDomainRootAlias());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		
		// mongo stores the array in JSON and resurrects back as ArrayList. 
		// Non-consequential for consumers interacting via JSON, such as UI-client or WS-client  
		assertTrue(CollectionUtils.isEqualCollection(K_list, (List<String>)audit.get(0).getNewValue()));
		
		assertEquals("/sample_core/attr_list_String", audit.get(0).getPropertyPath());
		assertEquals("ArrayList", audit.get(0).getPropertyType());
	
	}
	
	@Test
	public void t10_complexObject() {
		Param<ComplexObject> cp = _q.getRoot().findParamByPath("/sample_core/complex_object");
		assertNotNull(cp);
		assertNull(cp.getState());
		
		assertTrue(cp.isLeaf());
		
		assertNotNull(cp.getConfig().getEventHandlerConfig().findOnStateChangeHandler(
				FieldUtils.getField(SampleCoreEntity.class, "complex_object", true).getAnnotation(Audit.class)));
		
		// change value
		final ComplexObject k = new ComplexObject();
		k.setField1("Five");
		k.setField2(5);
		cp.setState(k);
		assertSame(k, cp.getState());
		
		Long coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getDomainRootRefId());
		assertNull((ComplexObject) audit.get(0).getPreviousValue());
		assertEquals(k.getField1(), ((ComplexObject) audit.get(0).getNewValue()).getField1());
		assertEquals(k.getField2(), ((ComplexObject) audit.get(0).getNewValue()).getField2());
		assertEquals("/sample_core/complex_object", audit.get(0).getPropertyPath());
		assertEquals("SampleCoreEntity.ComplexObject", audit.get(0).getPropertyType());
		assertNotNull(audit.get(0).getCreatedDate());
	}
	
	@Test
	public void audit_arrayType() {
		t08_core_string_array();
		Param<String[]> cp = _q.getRoot().findParamByPath("/sample_core/attr_array_String");
		//update the array with the same value - test to check if setting the state with the same array is logging additional audit entry
		final String arr_val[] = new String[]{"A", "B", "C @"+new Date()};
		cp.setState(arr_val);
		
		List<AuditEntry> audit_new = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit_new.size());
	}
}
