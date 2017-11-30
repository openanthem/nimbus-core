package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessConditionalStateEventHandlerHttpTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	public void t03_accessConditionalReadOnly() throws Exception {
		String userLoginId = createClientUserWithRoles("harvey","intake");
		
		Param<?> p = excuteNewConfigCore(userLoginId);
		assertNotNull(p);
		
		Param<?> accessParam = p.findParamByPath("/accessConditional_WhenAuthorities_Hidden1");
		
		assertFalse(accessParam.isVisible());
		assertFalse(accessParam.isEnabled());
	}
	
	@Test
	public void t04_accessConditionalHidden() throws Exception {
		String userLoginId = createClientUserWithRoles("batman","intake","clinician");
		
		Param<?> p = excuteNewConfigCore(userLoginId);
		assertNotNull(p);
		
		Param<?> accessParam = p.findParamByPath("/accessConditional_WhenAuthorities_Read2");
		
		assertTrue(accessParam.isVisible());
		assertTrue(accessParam.isEnabled());
	}
	
//	@Test
//	public void t04_accessConditionalGridLinkHidden() throws Exception {
//		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
//		
//		Param<?> p = excuteNewConfigView(userLoginId);
//		assertNotNull(p);
//		
//		Param<?> accessParam = p.findParamByPath("/accessConditional_Contains_Hidden2");
//		
//		assertFalse(accessParam.isVisible());
//		assertFalse(accessParam.isEnabled());
//	}

	@SuppressWarnings("unchecked")
	private Param<?> excuteNewConfigCore(String userLoginId) {
		final MockHttpServletRequest fetchUser = MockHttpRequestBuilder.withUri(USER_PARAM_ROOT)
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", "clientuser.loginId.eq('"+userLoginId+"')")
				.addParam("fetch","1")
				.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>) controller.handlePost(fetchUser, null);
		MultiOutput output = holder.getState();
		ClientUser clientuser = (ClientUser) output.getSingleResult();
		assertNotNull(clientuser);
		UserEndpointSession.setAttribute("client-user-key", clientuser);
		
		createResolvedAccessEntities(clientuser);

		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_PARAM_ACCESS_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
	
//	@SuppressWarnings("unchecked")
//	private Param<?> excuteNewConfigView(String userLoginId) {
//		final MockHttpServletRequest fetchUser = MockHttpRequestBuilder.withUri(USER_PARAM_ROOT)
//				.addAction(Action._search)
//				.addParam("fn", "query")
//				.addParam("where", "clientuser.loginId.eq('"+userLoginId+"')")
//				.addParam("fetch","1")
//				.getMock();
//		
//		Holder<MultiOutput> holder = (Holder<MultiOutput>) controller.handlePost(fetchUser, null);
//		MultiOutput output = holder.getState();
//		ClientUser clientuser = (ClientUser) output.getSingleResult();
//		assertNotNull(clientuser);
//		UserEndpointSession.setAttribute("client-user-key", clientuser);
//		
//		createResolvedAccessEntities(clientuser);
//
//		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ACCESS_ROOT)
//				.addAction(Action._new)
//				.getMock();
//
//		final Object resp = controller.handleGet(req, null);
//		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
//		return p;
//	}

	private void createResolvedAccessEntities(ClientUser clientuser) {
		Holder<MultiOutput> holder;
		MultiOutput output;
		Set<String> userRoleCodes = clientuser.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toSet());
		
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String userRole: userRoleCodes) {
			if(i == userRoleCodes.size() - 1) {
				sb.append("\"").append(userRole).append("\"");
			}
			else{
				sb.append("\"").append(userRole).append("\"").append(",");
			}
			i++;
			
		}
		
		final MockHttpServletRequest fetchUserRole = MockHttpRequestBuilder.withUri(USEREOLE_PARAM_ROOT)
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", "userrole.id.in("+sb.toString()+")")
				.getMock();
		
		holder = (Holder<MultiOutput>) controller.handlePost(fetchUserRole, null);
		output = holder.getState();
		List<ClientUserRole> clientuserRoles = (List<ClientUserRole>) output.getSingleResult();
		assertNotNull(clientuserRoles);
		
		Set<String> allAuthorityIds = new HashSet<>();
		
		for(ClientUserRole cUserRole: clientuserRoles) {
			allAuthorityIds.addAll(cUserRole.getAccessEntities());
		}
		
		StringBuilder sb2 = new StringBuilder();
		int j = 0;
		for(String authorityId: allAuthorityIds) {
			if(j == allAuthorityIds.size() - 1) {
				sb2.append("\"").append(authorityId).append("\"");
			}
			else{
				sb2.append("\"").append(authorityId).append("\"").append(",");
			}
			i++;
			
		}
		
		final MockHttpServletRequest fetchAuthorities = MockHttpRequestBuilder.withUri("/hooli/thebox/p/authorities")
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", "authorities.id.in("+sb2.toString()+")")
				.getMock();
		
		holder = (Holder<MultiOutput>) controller.handlePost(fetchAuthorities, null);
		output = holder.getState();
		List<ClientAccessEntity> authorities = (List<ClientAccessEntity>) output.getSingleResult();
		assertNotNull(authorities);
		
		
		List<String> resolvedAccessEntities = new ArrayList<>();
		//authorities.forEach((r) -> resolvedAccessEntities.addAll(r.getAccessEntities()));
		
		clientuser.setResolvedAccessEntities(authorities);
	}
	
	private String createClientUserWithRoles(String loginId, String... roles) {
		ClientUser cu = new ClientUser();
		cu.setLoginId(loginId);
		
		List<UserRole> userRoles = new ArrayList<>();
		Arrays.asList(roles).forEach((r) -> {
			
			ClientUserRole userRole = new ClientUserRole();
			userRole.setCode(r);
			userRole.setEffectiveDate(LocalDate.now());
			
			List<String> accessEntities = new ArrayList<>();
			
			ClientAccessEntity accessEntity = new ClientAccessEntity();
			accessEntity.setCode("member_management");
			
			mongo.save(accessEntity, "authorities");
			
			accessEntities.add(accessEntity.getId());
			
			userRole.setAccessEntities(accessEntities);
			
			mongo.save(userRole, "userrole");
			
			UserRole role = new UserRole();
			role.setRoleId(userRole.getId());
			role.setTerminationDate(LocalDate.now());
			userRoles.add(role);
		});
		cu.setRoles(userRoles);
		
		mongo.save(cu, "clientuser");
		
		return loginId;
	}
}
