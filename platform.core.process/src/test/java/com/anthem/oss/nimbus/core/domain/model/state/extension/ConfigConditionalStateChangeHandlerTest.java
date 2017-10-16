/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.util.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.audit.AuditEntry;


/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigConditionalStateChangeHandlerTest extends AbstractStateEventHandlerTests {

	private static final String CORE_CONDITIONAL_PARAM_PATH = "/sample_core/conditional_config_attr";
	
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
	public void t28_detached_domain_root() {
		// test with value from main domain root
	}
	

}
