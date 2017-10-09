/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.audit.AuditEntry;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.ui.VPSampleViewPageGreen;

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
		mapped_audit_string(p, "audit_String");
	}
	
	@Test
	public void t02_view_string() {
		Param<String> p = getView_audit_string();
		mapped_audit_string(p, "audit_String");
	}
	
	private void mapped_audit_string(Param<String> p, String property) {
		assertNull(p.getState());
		assertNull(p.findIfLeaf().getTransientOldState());

		// change value #1
		final String K_state_1 = "1. new value @ "+new Date();
		p.setState(K_state_1);
		
		String coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals(property, audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final String K_state_2 = "2. new value @ "+new Date();
		p.setState(K_state_2);
		assertEquals(K_state_1, p.findIfLeaf().getTransientOldState());
		
		audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals(property, audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals(coreRefId, audit.get(1).getEntityId());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(K_state_1, audit.get(1).getOldValue());
		assertEquals(property, audit.get(1).getProperty());
		assertNotNull(audit.get(1).getCreatedBy());
		assertNotNull(audit.get(1).getCreatedDate());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}
	
	
	@Test
	public void t03_core_integer() {
		Param<Integer> p = getCore_audit_integer();
		mapped_audit_integer(p);
	}
	
	@Test
	public void t04_view_integer() {
		Param<Integer> p = getView_audit_integer();
		mapped_audit_integer(p);
	}
	
	private void mapped_audit_integer(Param<Integer> p) {
		assertNull(p.getState());
		assertNull(p.findIfLeaf().getTransientOldState());

		// change value #1
		final Integer K_state_1 = 1;
		p.setState(K_state_1);
		
		String coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals("audit_Integer", audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final Integer K_state_2 = 2;
		p.setState(K_state_2);
		assertEquals(K_state_1, p.findIfLeaf().getTransientOldState());
		
		audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals("audit_Integer", audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals(coreRefId, audit.get(1).getEntityId());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(K_state_1, audit.get(1).getOldValue());
		assertEquals("audit_Integer", audit.get(1).getProperty());
		assertNotNull(audit.get(1).getCreatedBy());
		assertNotNull(audit.get(1).getCreatedDate());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}
	
	
	@Test
	public void t05_coreOnly_unmapped_String() {
		Param<String> p = getCore_unmapped_string();
		mapped_audit_string(p, "unmapped_String");
	}

	
	@Test
	public void t06_viewOnly_unmapped_String() {
		Param<String> p = getView_unmapped_string();
		String property = "unmapped_String";
	
		assertNull(p.getState());
		assertNull(p.findIfLeaf().getTransientOldState());

		// change value #1
		final String K_state_1 = "1. new value @ "+new Date();
		p.setState(K_state_1);
		
		String coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_view_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals(property, audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
	
		// change value #2
		final String K_state_2 = "2. new value @ "+new Date();
		p.setState(K_state_2);
		assertEquals(K_state_1, p.findIfLeaf().getTransientOldState());
		
		audit = mongo.findAll(AuditEntry.class, "sample_view_audit_history");
		assertEquals(2, audit.size());
		
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals(property, audit.get(0).getProperty());
		assertNotNull(audit.get(0).getCreatedBy());
		assertNotNull(audit.get(0).getCreatedDate());
		
		assertEquals(coreRefId, audit.get(1).getEntityId());
		assertEquals(K_state_2, audit.get(1).getNewValue());
		assertEquals(K_state_1, audit.get(1).getOldValue());
		assertEquals(property, audit.get(1).getProperty());
		assertNotNull(audit.get(1).getCreatedBy());
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
		assertNull(cp.findIfLeaf().getTransientOldState());
		
		assertNotNull(vp);
		assertNull(vp.getState());
		assertNull(vp.findIfLeaf().getTransientOldState());
		
		// change value #1 in view
		final String K_state_1 = "1. new value @ "+new Date();
		vp.setState(K_state_1);
		
		String coreRefId = _q.getCore().getState().getId();
		assertNotNull(coreRefId);
		
		assertSame(K_state_1, vp.getState());
		assertSame(K_state_1, cp.getState());
		
		List<AuditEntry> audit = mongo.findAll(AuditEntry.class, "sample_core_audit_history");
		assertEquals(1, audit.size());
		assertEquals(coreRefId, audit.get(0).getEntityId());
		assertEquals(K_state_1, audit.get(0).getNewValue());
		assertEquals(null, audit.get(0).getOldValue());
		assertEquals("audit_nested_attr", audit.get(0).getProperty());
		
		// validate that view based audit collection is not created
		assertFalse(mt.collectionExists("samepl_view_audit_history"));
	}

	
}
