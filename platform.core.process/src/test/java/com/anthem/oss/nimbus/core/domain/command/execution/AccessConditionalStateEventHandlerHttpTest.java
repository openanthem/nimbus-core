package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
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
		
		Param<?> p = excuteNewConfig(userLoginId);
		assertNotNull(p);
		
		Param<Boolean> visibleParam = p.findParamByPath("/accessConditional_Contains_Read/#/visible");
		Param<Boolean> enableParam = p.findParamByPath("/accessConditional_Contains_Read/#/enabled");
		
		assertTrue(visibleParam.getState() == true);
		assertTrue(enableParam.getState() != true);
	}
	
	@Test
	public void t04_accessConditionalHidden() throws Exception {
		String userLoginId = createClientUserWithRoles("batman","intake","clinician");
		
		Param<?> p = excuteNewConfig(userLoginId);
		assertNotNull(p);
		
		Param<Boolean> visibleParam = p.findParamByPath("/accessConditional_Contains_Hidden2/#/visible");
		Param<Boolean> enableParam = p.findParamByPath("/accessConditional_Contains_Hidden2/#/enabled");
		
		assertTrue(visibleParam.getState() == false);
		assertTrue(enableParam.getState() == null);
	}

	@SuppressWarnings("unchecked")
	private Param<?> excuteNewConfig(String userLoginId) {
		final MockHttpServletRequest fetchModifiedCoreDomain = MockHttpRequestBuilder.withUri(USER_PARAM_ROOT)
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", "clientuser.loginId.eq('"+userLoginId+"')")
				.addParam("fetch","1")
				.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>) controller.handlePost(fetchModifiedCoreDomain, null);
		MultiOutput output = holder.getState();
		ClientUser clientuser = (ClientUser) output.getSingleResult();
		assertNotNull(clientuser);
		UserEndpointSession.setAttribute("client-user-key", clientuser);

		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_PARAM_ACCESS_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
	
	private String createClientUserWithRoles(String loginId, String... roles) {
		ClientUser cu = new ClientUser();
		cu.setLoginId(loginId);
		
		List<UserRole> userRoles = new ArrayList<>();
		Arrays.asList(roles).forEach((r) -> {
			UserRole role = new UserRole();
			role.setRoleCode(r);
			role.setRetiredDate(LocalDate.now());
			userRoles.add(role);
		});
		cu.setRoles(userRoles);
		
		mongo.save(cu, "clientuser");
		
		return loginId;
	}
}
