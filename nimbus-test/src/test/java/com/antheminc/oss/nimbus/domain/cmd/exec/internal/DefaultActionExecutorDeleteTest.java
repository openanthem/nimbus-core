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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultTransientParamState;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorDeleteTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	MongoOperations mongoOps;
	
	@Test
	public void t1_colElem_add() {
		Long refId = createOrGetDomainRoot_RefId();
		
		// create new collection
		MockHttpServletRequest colNew_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
					.addNested("/page_green/tile/list_attached_noConversion_NestedEntity").addAction(Action._new).getMock();
		
		SampleCoreNestedEntity colElemState_0 = new SampleCoreNestedEntity();
		colElemState_0.setNested_attr_String("0. TEST_INTG_COL_ELEM_add "+ new Date());
		
		SampleCoreNestedEntity colElemState_1 = new SampleCoreNestedEntity();
		colElemState_1.setNested_attr_String("1. TEST_INTG_COL_ELEM_add "+ new Date());
		
		List<SampleCoreNestedEntity> colState = new ArrayList<SampleCoreNestedEntity>();
		colState.add(colElemState_0);
		colState.add(colElemState_1);
		
		String jsonPayload = converter.toJson(colState);
		
		Object colNew_Resp = controller.handlePost(colNew_Req, jsonPayload);
		assertNotNull(colNew_Resp);
		
		// db validation - after add new collection
		SampleCoreEntity core = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colState.size(), core.getAttr_list_1_NestedEntity().size());
		assertEquals(colState.get(0).getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());
		assertEquals(colState.get(1).getNested_attr_String(), core.getAttr_list_1_NestedEntity().get(1).getNested_attr_String());
		
		// do delete of '0'the elem so it reduces the size but elem left would have elemId of '1'
		MockHttpServletRequest colElemDelete_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/tile/list_attached_noConversion_NestedEntity/0").addAction(Action._delete).getMock();
		
		Object colDelete_Resp = controller.handleDelete(colElemDelete_Req, null);
		assertNotNull(colDelete_Resp);
		
		// db validation - after delete
		SampleCoreEntity coreAfter = mongo.findById(refId, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		assertEquals(colState.size()-1, coreAfter.getAttr_list_1_NestedEntity().size());
		// validate that the earlier "index[1]" elem is now available as "index[0]"
		assertEquals(colState.get(1).getNested_attr_String(), coreAfter.getAttr_list_1_NestedEntity().get(0).getNested_attr_String());


		// object validation - after delete
		MockHttpServletRequest colGet_Req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_1_NestedEntity").addAction(Action._get).getMock();
		
		Object colGet_Resp = controller.handleGet(colGet_Req, null);
		ListParam<SampleCoreNestedEntity> pListNested = ExtractResponseOutputUtils.extractOutput(colGet_Resp);
		assertNotNull(pListNested);
		
		assertEquals(colState.size()-1, pListNested.size());
		assertEquals(colState.get(1).getNested_attr_String(), pListNested.findParamByPath("/1/nested_attr_String").getState());

	}

	@Test
	public void t02_sessionDelete() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId).addAction(Action._get).getMock();
		Object resp1 = controller.handleGet(req1, null);
		assertNotNull(resp1);
		
		String sessionKey = new StringBuilder().append("{/").append(VIEW_DOMAIN_ALIAS).append(":").append(refId).append("}").toString();
		assertNotNull(sessionProvider.getAttribute(sessionKey));
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/page_green/audit_String").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test"));
		
		MockHttpServletRequest req2 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId).addParam("cache", "flush").addAction(Action._delete).getMock();
		Object resp2 = controller.handleGet(req2, null);
		assertNotNull(resp2);
		
		assertNull(sessionProvider.getAttribute(sessionKey));
		
		MockHttpServletRequest req3 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId).addAction(Action._get).getMock();
		Object resp3 = controller.handleGet(req3, null);
		assertNotNull(resp3);

		assertNotNull(sessionProvider.getAttribute(sessionKey));

	}
	
	@Test
	public void t03_sessionDelete() {
		Long refId = createOrGetDomainRoot_RefId();
		MockHttpServletRequest req1 = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId).addAction(Action._get).getMock();
		Object resp1 = controller.handleGet(req1, null);
		assertNotNull(resp1);
		
		String sessionKey = new StringBuilder().append("{/").append(VIEW_DOMAIN_ALIAS).append(":").append(refId).append("}").toString();
		assertNotNull(sessionProvider.getAttribute(sessionKey));
		
		
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/").addAction(Action._update).getMock();
		Object resp = controller.handlePut(request, null, converter.toJson("test"));
		assertNotNull(resp);
		
		MockHttpServletRequest req4 = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId).addParam("cache", "flush").addAction(Action._delete).getMock();
		Object resp4 = controller.handleGet(req4, null);
		assertNotNull(resp4);
		
		Query query = new Query(Criteria.where("_id").is(refId));
		SampleCoreEntity sampleCore = mongoOps.findOne(query, SampleCoreEntity.class, CORE_DOMAIN_ALIAS);
		sampleCore.setAttr_String("direct update");
		mongoOps.save(sampleCore, CORE_DOMAIN_ALIAS);
		
		MockHttpServletRequest req3 = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT).addRefId(refId).addNested("/attr_String").addAction(Action._get).getMock();
		Object resp3 = controller.handleGet(req3, null);
		assertNotNull(resp3);
		Param<List<String>> p = ParamUtils.extractResponseByParamPath(resp3, "/attr_String");
		assertEquals(p.getState(),"direct update");
	}
}
