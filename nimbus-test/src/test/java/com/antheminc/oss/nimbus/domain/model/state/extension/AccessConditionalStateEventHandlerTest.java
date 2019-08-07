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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.user.UserRole;

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
	
	@Autowired SessionProvider sessionProvider;
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_core_access/_new").getCommand();
		return cmd;
	}
	
	@Override
	public void before() {
		sessionProvider.setAttribute("client-user-key", CU);
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
			role.setRoleCode(r);
			role.setTerminationDate(LocalDate.now());
			userRoles.add(role);
		});
		cu.setRoles(userRoles);
		cu.setResolvedAccessEntities(accessEntities); // This step simulates that for a user we have all the resolved access entities de-normalized here
		return cu;
	}

}
