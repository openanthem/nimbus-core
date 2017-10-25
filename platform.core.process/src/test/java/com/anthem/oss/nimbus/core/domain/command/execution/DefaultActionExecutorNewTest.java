package com.anthem.oss.nimbus.core.domain.command.execution;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
 
import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.DefaultUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;
 
 
/**
 * @author Soham Chakravarti
 * @author Sandeep Mantha
 * @author Rakesh Patel
*/
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorNewTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void t00_json() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();

		mvc.perform(post(req.getRequestURI()).content("{}").contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.0.result.outputs[0].value", IsNull.notNullValue())).andReturn()
				.getResponse().getContentAsString();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void t01_ruleExecution() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addAction(Action._new).getMock();

		final Object resp = controller.handleGet(req, null);
		Object ob = ExtractResponseOutputUtils.extractOutput(resp);
		assertNotNull(ob);

		final MockHttpServletRequest fetchModifiedCoreDomain = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT)
				.addAction(Action._search).addParam("fn", "query").getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>) controller.handlePost(fetchModifiedCoreDomain, null);
		MultiOutput output = holder.getState();
		List<SampleCoreEntity> core = (List<SampleCoreEntity>) output.getSingleResult();
		assertEquals(core.size(), 2);
		assertEquals(core.get(0).getAttr_String(), "coreRuleUpdate");
		assertEquals(core.get(1).getAttr_String(), "coreRuleUpdate");

	}
	
	@Test
	public void t03_accessConditionalReadOnly() throws Exception {
		String userLoginId = createClientUserWithRoles("harvey","hero");
		
		Param<?> p = excuteNewConfig(userLoginId);
		assertNotNull(p);
		
		Param<Boolean> visibleParam = p.findParamByPath("/q1Level1/#/visible");
		Param<Boolean> enableParam = p.findParamByPath("/q1Level1/#/enabled");
		
		assertTrue(visibleParam.getState() == true);
		assertTrue(enableParam.getState() != true);
	}
	
	@Test
	public void t04_accessConditionalReadWrite() throws Exception {
		String userLoginId = createClientUserWithRoles("batman","hero","superhero");
		
		Param<?> p = excuteNewConfig(userLoginId);
		assertNotNull(p);
		
		Param<Boolean> visibleParam = p.findParamByPath("/q1Level1/#/visible");
		Param<Boolean> enableParam = p.findParamByPath("/q1Level1/#/enabled");
		
		assertTrue(visibleParam.getState() == true);
		assertTrue(enableParam.getState() == true);
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
		DefaultUser pu = new DefaultUser();
		pu.setLoginId(loginId);
		mongo.save(pu, "defaultUser");

		ClientUser cu = new ClientUser();
		cu.setPlatformUser(pu);
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