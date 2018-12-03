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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.entity.client.access.ClientUserRole;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.user.UserRole;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntityAccess;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleTask;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.SampleCoreEntityAccessLineItem;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorSearchHttpTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired SessionProvider sessionProvider;


	// Rakesh :TODO: - more test cases to capture different scenarios of page/filter
	@Test
	public void t01_GridPaginationOnly() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("mested_test1");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addAction(Action._get)
				.getMock();
		//final Object gridResponse = controller.handlePost(gridRequest, "[{\"code\":\"attr_String2\", \"value\":\"test2_string2\"},{\"code\":\"nested_attr_String\", \"value\":\"test1\"},{\"code\":\"attr_LocalDate1\",\"value\":\""+LocalDate.now()+"T00:00:00.000Z\"}]");
		final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
			}
		}
	}
	
	@Test
	public void t02_GridPaginationWithSortOnTextField() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("mested_test1");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				//.addParam("pageCriteria", "pageSize=5&page=0&sortBy=attr_String,ASC")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addParam("sortBy", "attr_String,ASC")
				.addAction(Action._get)
				.getMock();
		//final Object gridResponse = controller.handlePost(gridRequest, "[{\"code\":\"attr_String2\", \"value\":\"test2_string2\"},{\"code\":\"nested_attr_String\", \"value\":\"test1\"},{\"code\":\"attr_LocalDate1\",\"value\":\""+LocalDate.now()+"T00:00:00.000Z\"}]");
		final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
				assertEquals(2, pg.getContent().size());
			}
		}
	}
	
	@Test
	public void t03_GridPaginationWithSortOnDateField() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now().plusDays(1));
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("mested_test1");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				//.addParam("pageCriteria", "pageSize=5&page=0&sortBy=attr_String,ASC")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addParam("sortBy", "attr_LocalDate1,ASC")
				.addAction(Action._get)
				.getMock();
		//final Object gridResponse = controller.handlePost(gridRequest, "[{\"code\":\"attr_String2\", \"value\":\"test2_string2\"},{\"code\":\"nested_attr_String\", \"value\":\"test1\"},{\"code\":\"attr_LocalDate1\",\"value\":\""+LocalDate.now()+"T00:00:00.000Z\"}]");
		final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
				assertEquals(2, pg.getContent().size());
				
				assertEquals(LocalDate.now(), ((SampleCoreEntityAccessLineItem)pg.getContent().get(0)).getAttr_LocalDate1());
				assertEquals(LocalDate.now().plusDays(1), ((SampleCoreEntityAccessLineItem)pg.getContent().get(1)).getAttr_LocalDate1());
				
			}
		}
	}
	
	@Test
	public void t04_GridPaginationWithFilterOnTextField() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("nested_test2");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				//.addParam("pageCriteria", "pageSize=5&page=0&sortBy=attr_String,ASC")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addAction(Action._get)
				.getMock();
		final Object gridResponse = controller.handlePost(gridRequest, "{\"filters\": [{\"code\":\"attr_String2\", \"value\":\"test2_string2\"},{\"code\":\"nested_attr_String\", \"value\":\"test1\"}]}");
		//final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
				assertEquals(1, pg.getContent().size());
				
			}
		}
	}
	
	@Test
	public void t05_GridPaginationWithFilterOnDateField() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now().plusDays(2));
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("mested_test1");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				//.addParam("pageCriteria", "pageSize=5&page=0&sortBy=attr_String,ASC")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addParam("sortBy", "attr_String,ASC")
				.addAction(Action._get)
				.getMock();
		final Object gridResponse = controller.handlePost(gridRequest, "{\"filters\": [{\"code\":\"attr_LocalDate1\",\"value\":\""+LocalDate.now()+"T00:00:00.000Z\"}]}");
		//final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
				assertEquals(1, pg.getContent().size());
			}
		}
	}
	

	@Test
	public void t06_GridPaginationWithSortFilter() throws Exception {
		String userLoginId = createClientUserWithRoles("superman","intake","clinician");
		
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		
		SampleCoreNestedEntity nestedEntity = new SampleCoreNestedEntity();
		nestedEntity.setNested_attr_String("nested_test1");
		scea.setAccessConditional_Contains_Hidden1(nestedEntity);
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test2_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now().plusDays(2));
		
		SampleCoreNestedEntity nestedEntity2 = new SampleCoreNestedEntity();
		nestedEntity2.setNested_attr_String("mested_test1");
		scea2.setAccessConditional_Contains_Hidden1(nestedEntity2);
		
		mongo.save(scea, "sample_core_access");
		mongo.save(scea2, "sample_core_access");
		
		
		Param<?> p = excuteNewConfigView(userLoginId);
		assertNotNull(p);
		
		Long refId = p.findStateByPath("/.m/id");
		
		final MockHttpServletRequest gridRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addRefId(refId)
				.addNested("/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities")
				//.addParam("pageCriteria", "pageSize=5&page=0&sortBy=attr_String,ASC")
				.addParam("pageSize", "5")
				.addParam("page", "0")
				.addParam("sortBy", "attr_String,ASC")
				.addAction(Action._get)
				.getMock();
		final Object gridResponse = controller.handlePost(gridRequest, "{\"filters\": [{\"code\":\"attr_String2\", \"value\":\"test2_string2\"},{\"code\":\"nested_attr_String\", \"value\":\"test1\"},{\"code\":\"attr_LocalDate1\",\"value\":\""+LocalDate.now()+"T00:00:00.000Z\"}]}");
		//final Object gridResponse = controller.handlePost(gridRequest, null);
		assertNotNull(gridResponse);
		
		List<Output<?>> outputs = MultiOutput.class.cast(Holder.class.cast(gridResponse).getState()).getOutputs();
		
		assertNotNull(outputs);
		
		for(Output<?> op: outputs) {
			if(op.getValue() instanceof ListParam<?>) {
				ListParam<?> param = (ListParam<?>)op.getValue();
				Page<?> pg = param.getPage();
				assertNotNull(pg);
				assertNotNull(pg.getContent());
				assertEquals(1, pg.getContent().size());
			}
		}
	}
	
	@Test
	public void testLookupSort() {
		SampleTask task1 = new SampleTask();
		task1.setId(1L);
		task1.setTaskName("Play");
		SampleTask task2 = new SampleTask();
		task2.setId(2L);
		task2.setTaskName("Groom");
		mongo.insert(task1, "sampletask");
		mongo.insert(task2, "sampletask");
		
		Long refId = createOrGetDomainRoot_RefId();
		
		final MockHttpServletRequest unsortedRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/unsortedDynamicValues")
				.addAction(Action._get)
				.getMock();
		final Object unsortedResponse = controller.handlePost(unsortedRequest, null);
		Param<List<String>> pUnsorted = ParamUtils.extractResponseByParamPath(unsortedResponse, "/unsortedDynamicValues");
		Assert.assertEquals(2, pUnsorted.getValues().size());
		Assert.assertEquals("1", pUnsorted.getValues().get(0).getCode());
		Assert.assertEquals("Play", pUnsorted.getValues().get(0).getLabel());
		Assert.assertEquals("2", pUnsorted.getValues().get(1).getCode());
		Assert.assertEquals("Groom", pUnsorted.getValues().get(1).getLabel());
		
		final MockHttpServletRequest sortedRequest = MockHttpRequestBuilder
				.withUri(VIEW_PARAM_ROOT)
				.addRefId(refId)
				.addNested("/page_green/tile/view_sample_form/sortedDynamicValues")
				.addAction(Action._get)
				.getMock();
		final Object sortedResponse = controller.handlePost(sortedRequest, null);
		Param<List<String>> pSorted = ParamUtils.extractResponseByParamPath(sortedResponse, "/sortedDynamicValues");
		Assert.assertEquals(2, pSorted.getValues().size());
		Assert.assertEquals("2", pSorted.getValues().get(0).getCode());
		Assert.assertEquals("Groom", pSorted.getValues().get(0).getLabel());
		Assert.assertEquals("1", pSorted.getValues().get(1).getCode());
		Assert.assertEquals("Play", pSorted.getValues().get(1).getLabel());
	}
	
	@SuppressWarnings("unchecked")
	private Param<?> excuteNewConfigView(String userLoginId) {
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
		sessionProvider.setAttribute("client-user-key", clientuser);
		
		createResolvedAccessEntities(clientuser);

		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ACCESS_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}

	private void createResolvedAccessEntities(ClientUser clientuser) {
		Holder<MultiOutput> holder;
		MultiOutput output;
		Set<String> userRoleCodes = clientuser.getRoles().stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
		
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
				.addParam("where", "userrole.code.in("+sb.toString()+")")
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
				.addParam("where", "authorities.code.in("+sb2.toString()+")")
				.getMock();
		
		holder = (Holder<MultiOutput>) controller.handlePost(fetchAuthorities, null);
		output = holder.getState();
		List<ClientAccessEntity> authorities = (List<ClientAccessEntity>) output.getSingleResult();
		assertNotNull(authorities);
		
		
		clientuser.setResolvedAccessEntities(authorities);
	}
	
	private String createClientUserWithRoles(String loginId, String... roles) {
		ClientUser cu = new ClientUser();
		cu.setId(new Random().nextLong());
		cu.setLoginId(loginId);
		
		List<UserRole> userRoles = new ArrayList<>();
		Arrays.asList(roles).forEach((r) -> {
			
			ClientUserRole userRole = new ClientUserRole();
			userRole.setId(new Random().nextLong());
			userRole.setCode(r);
			userRole.setEffectiveDate(LocalDate.now());
			
			List<String> accessEntities = new ArrayList<>();
			
			ClientAccessEntity accessEntity = new ClientAccessEntity();
			accessEntity.setId(new Random().nextLong());
			accessEntity.setCode("member_management");
			
			mongo.save(accessEntity, "authorities");
			
			accessEntities.add(accessEntity.getCode());
			
			userRole.setAccessEntities(accessEntities);
			
			mongo.save(userRole, "userrole");
			
			UserRole role = new UserRole();
			role.setRoleCode(userRole.getCode());
			role.setTerminationDate(LocalDate.now());
			userRoles.add(role);
		});
		cu.setRoles(userRoles);
		
		mongo.save(cu, "clientuser");
		
		return loginId;
	}
}
