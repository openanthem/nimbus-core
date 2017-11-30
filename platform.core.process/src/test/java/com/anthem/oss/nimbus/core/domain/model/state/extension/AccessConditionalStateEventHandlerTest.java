package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	private static final String SAMPLE_CORE_ACCESS_ENTITY = "/sample_core_access";
	
	private static final String ACCESS_CONDITIONAL_CONTAINS_HIDDEN_PARAM1_PATH = "/sample_core_access/accessConditional_Contains_Hidden1";
	private static final String ACCESS_CONDITIONAL_CONTAINS_HIDDEN_PARAM2_PATH = "/sample_core_access/accessConditional_Contains_Hidden2";
	private static final String ACCESS_CONDITIONAL_CONTAINS_READ_PARAM_PATH = "/sample_core_access/accessConditional_Contains_Read";
	
	private static final String ACCESS_CONDITIONAL_WHEN_READ_PARAM1_PATH = "/sample_core_access/accessConditional_WhenRoles_Read1";
	private static final String ACCESS_CONDITIONAL_WHEN_READ_PARAM2_PATH = "/sample_core_access/accessConditional_WhenRoles_Read2";
	
	private static final String ACCESS_CONDITIONAL_WHEN_HIDDEN_PARAM1_PATH = "/sample_core_access/accessConditional_WhenRoles_Hidden1";
	private static final String ACCESS_CONDITIONAL_WHEN_HIDDEN_PARAM2_PATH = "/sample_core_access/accessConditional_WhenRoles_Hidden2";
	
	private static final String ACCESS_CONDITIONAL_WHENAUTHORITIES_HIDDEN_PARAM_PATH = "/sample_core_access/accessConditional_WhenAuthorities_Hidden1";
	private static final String ACCESS_CONDITIONAL_WHENAUTHORITIES_READ_PARAM2_PATH = "/sample_core_access/accessConditional_WhenAuthorities_Read2";
	private static final String ACCESS_CONDITIONAL_WHENAUTHORITIES_HIDDEN_PARAM2_PATH = "/sample_core_access/accessConditional_WhenAuthorities_Hidden2";
	
	
	
	
	private static ClientUser CU = createClientUserWithRoles("batman","intake","clinician");
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_core_access/_new").getCommand();
		return cmd;
	}
	
	@Override
	public void before() {
		UserEndpointSession.setAttribute("client-user-key", CU);
		super.before();
	}
	
	@Test
	public void t0_initCheck() {
		assertNotNull("Command cannot be null", _q.getRoot().findParamByPath(SAMPLE_CORE_ACCESS_ENTITY));
	}
	
	@Test
	public void t1_hidden_ContainsTrue_OneRole() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_CONTAINS_HIDDEN_PARAM1_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t2_hidden_ContainsTrue_MultipleRoles() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_CONTAINS_HIDDEN_PARAM2_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t3_readOnly_ContainsTrue() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_CONTAINS_READ_PARAM_PATH);
		assertTrue(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t4_readOnly_WhenRolesTrue() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHEN_READ_PARAM1_PATH);
		assertTrue(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t5_notReadOnly_WhenRolesFails() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHEN_READ_PARAM2_PATH);
		assertTrue(p.isVisible());
		assertTrue(p.isEnabled());
	}
	
	@Test
	public void t6_hidden_WhenRolesTrue() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHEN_HIDDEN_PARAM1_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t7_hidden_WhenRolesTrue() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHEN_HIDDEN_PARAM2_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	

	@Test
	public void t8_hidden_WhenAuthoritiesTrue() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHENAUTHORITIES_HIDDEN_PARAM_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t9_hidden_WhenAuthoritiesFalse() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHENAUTHORITIES_READ_PARAM2_PATH);
		assertFalse(p.isVisible());
		assertFalse(p.isEnabled());
	}
	
	@Test
	public void t10_hidden_WhenAuthoritiesFalse() {
		Param<?> p = _q.getRoot().findParamByPath(ACCESS_CONDITIONAL_WHENAUTHORITIES_HIDDEN_PARAM2_PATH);
		assertTrue(p.isVisible());
		assertTrue(p.isEnabled());
	}
	
	
	
	private static ClientUser createClientUserWithRoles(String loginId, String... roles) {
		
		ClientAccessEntity accessEntity2 = new ClientAccessEntity();
		accessEntity2.setCode("member_management");
		
		List<ClientAccessEntity> accessEntities = new ArrayList<>();
		accessEntities.add(accessEntity2);
		
		ClientUser cu = new ClientUser();
		cu.setLoginId(loginId);
		
		List<UserRole> userRoles = new ArrayList<>();
		Arrays.asList(roles).forEach((r) -> {
			UserRole role = new UserRole();
			role.setRoleId(r);
			role.setTerminationDate(LocalDate.now());
			userRoles.add(role);
		});
		cu.setRoles(userRoles);
		cu.setResolvedAccessEntities(accessEntities); // This step simulates that for a user we have all the resolved access entities de-normalized here
		return cu;
	}

}
