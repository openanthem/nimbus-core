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
package com.antheminc.oss.nimbus.domain.model.state.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.extension.StaticCodeValueBasedCodeToLabelConverter;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractListPaginatedParam.PageWrapper.PageRequestAndRespone;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;
import com.antheminc.oss.nimbus.entity.VStaticCodeValue;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.queue.Queue;
import com.antheminc.oss.nimbus.entity.user.ClientUserGroup;
import com.antheminc.oss.nimbus.entity.user.GroupUser;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntityAccess;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleDomain;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleNestedDomain;


/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ContextConfiguration(classes = MongoConfiguration.class)
@SuppressWarnings("unchecked")
public class DefaultActionExecutorSearchTest extends AbstractFrameworkIntegrationTests {
	
	protected static final String SAMPLE_CORE_ENTITY_ACCESS_ALIAS = "sample_core_access";
	protected static final String CLIENT_USER_ALIAS = "clientuser";
	protected static final String CLIENT_USER_GROUP_ALIAS = "clientusergroup";
	protected static final String QUEUE_ALIAS = "queue";
	protected static final String STATIC_CODE_VALUE_ALIAS = "staticCodeValue";
	
	private static final String[] COLLECTIONS = {QUEUE_ALIAS, CLIENT_USER_ALIAS, CLIENT_USER_GROUP_ALIAS,SAMPLE_CORE_ENTITY_ACCESS_ALIAS};
	
	@Autowired
	MongoOperations mongoOps;
	
	@Autowired
	StaticCodeValueBasedCodeToLabelConverter labelConverter;
	
	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	@Test
	public void t1_testSearchByLookupStaticCodeValue() {
		this.mongoOps.dropCollection("staticCodeValue");
		
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue("code1", "label1", "desc1"));
		final StaticCodeValue expected = new StaticCodeValue("/status0", expectedValues);
		expected.setId(new Random().nextLong());
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/status0')");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue();
		assertEquals(1, values.size());
		assertEquals(expectedValues.get(0).getCode(), values.get(0).getCode());
		assertEquals(expectedValues.get(0).getLabel(), values.get(0).getLabel());
		assertEquals(expectedValues.get(0).getDesc(), values.get(0).getDesc());
	}
	
	/**
	 * This test method shows how to sort the Param Values of staticCodeValue search in descending order in memory (once mongoDB result is available).
	 * The orderby property expects the value in propertyName.direction pattern.
	 * <p> e.g. if you want to sort the param values in descending order based on <code>code</code> property of <code>ParamValue</code> class, 
	 * you would use below configuration.
	 */
	@Test
	public void t0_testSearchByLookupStaticCodeValue_inMemorySorted() {
		this.mongoOps.dropCollection("staticCodeValue");
		
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue(Long.valueOf(1), "label1", "desc1"));
		expectedValues.add(new ParamValue(Long.valueOf(2), "label2", "desc2"));
		final StaticCodeValue expected = new StaticCodeValue("/status1", expectedValues);
		expected.setId(new Random().nextLong());
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/status1')&orderby=code.desc()");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue();
		assertEquals(2, values.size());
		assertEquals(expectedValues.get(1).getCode(), values.get(0).getCode());
		assertEquals(expectedValues.get(1).getLabel(), values.get(0).getLabel());
		
		assertEquals(expectedValues.get(0).getCode(), values.get(1).getCode());
		assertEquals(expectedValues.get(0).getLabel(), values.get(1).getLabel());
	}
	
	/**
	 * This test method shows how to sort staticCodeValue search in MongoDB.
	 * The orderby property expects the value in domainAlias.propertyName.direction pattern.
	 * <p> e.g. if you want to sort the staticCodeValues in descending order based on <code>paramCode</code> property of <code>StaticCodeValue</code> class, 
	 * you would use below configuration.
	 */
	@Test
	public void testSearchByLookupStaticCodeValue_dbsorted() {
		this.mongoOps.dropCollection("staticCodeValue");
		
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue(Long.valueOf(1), "slabel1", "sdesc1"));
		expectedValues.add(new ParamValue(Long.valueOf(2), "slabel2", "sdesc2"));
		final StaticCodeValue expected = new StaticCodeValue("/status1", expectedValues);
		expected.setId(new Random().nextLong());
		this.mongoOps.insert(expected, "staticCodeValue");
		
		final List<ParamValue> expectedValues2 = new ArrayList<>();
		expectedValues2.add(new ParamValue(Long.valueOf(1), "clabel1", "cdesc1"));
		expectedValues2.add(new ParamValue(Long.valueOf(2), "clabel2", "cdesc2"));
		final StaticCodeValue expected2 = new StaticCodeValue("/category", expectedValues);
		expected2.setId(new Random().nextLong());
		this.mongoOps.insert(expected2, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=query&orderby=staticCodeValue.paramCode.desc()");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<StaticCodeValue> values = (List<StaticCodeValue>)ops.get(0).getValue();
		assertEquals(2, values.size());
		assertEquals(expected.getParamCode(), values.get(0).getParamCode());
		assertEquals(expected2.getParamCode(), values.get(1).getParamCode());
		
		cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=query&orderby=staticCodeValue.paramCode.asc()");
		
		multiOp = this.commandGateway.execute(cmdMsg);
		ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<StaticCodeValue> values2 = (List<StaticCodeValue>)ops.get(0).getValue();
		assertEquals(2, values2.size());
		assertEquals(expected2.getParamCode(), values2.get(0).getParamCode());
		assertEquals(expected.getParamCode(), values2.get(1).getParamCode());
	
	}
	
	@Test
	public void t11_testSearchByLookupStaticCodeValueElemMatch() {
		this.mongoOps.dropCollection("staticCodeValue");
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue("ACL", "Anticardiolpin Antibodies", null));
		final StaticCodeValue expected = new StaticCodeValue("anything", expectedValues);
		expected.setId(new Random().nextLong());
		this.mongoOps.insert(expected, "staticCodeValue");
		
		assertEquals("Anticardiolpin Antibodies", this.labelConverter.serialize("ACL"));
	}

	@Test
	public void t2_testSearchByLookupModel() {
		insertClient();
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/client/_search?fn=lookup&projection.mapsTo=code:name,label:name");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue(); // TODO having to cast the output, is that correct ??
		assertNotEquals(0, values.size());
		
		values.forEach((v)->System.out.println(v.getCode()));
	}
	
	@Test
	public void t3_testSearchByExampleCriteriaNull() {
		insertClient();
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/client/_search?fn=example");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<?> values = (List<?>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals(2, values.size());
	}
	
	@Test
	public void t4_testSearchByExampleCriteriaNotNull() {
		Client c = insertClient();
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/client/_search?fn=example");
		cmdMsg.setRawPayload("{\"code\":\""+c.getCode()+"\"}");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<?> values = (List<?>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals(2, values.size());
	}
	
	@Test
	public void t41_testSearchByQueryCriteriaNotNull() {
		insertClient();
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/client/_search?fn=query&where=client.code.eq('c1')&projection.mapsTo=test:name");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<?> values = (List<?>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals(1, values.size());
	}

	@Test
	public void t5_testSearchByQueryWithProjection() {
		this.mongoOps.dropCollection("staticCodeValue");
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue("code1", "label1", "desc1"));
		final StaticCodeValue expected = new StaticCodeValue("/status", expectedValues);
		expected.setId(new Random().nextLong());
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&projection.alias=vstaticCodeValue");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		List<VStaticCodeValue> values = (List<VStaticCodeValue>) multiOp.getSingleResult();
		
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("/status", values.get(0).getParamCode());
	}
	
	
	@Test
	public void test_dynamicLookUpWithProjection() {
		SampleDomain entity1 = new SampleDomain();
		SampleNestedDomain nestedEntity1 = new SampleNestedDomain();
		entity1.setId(2L);
		entity1.setAttr_String("searchString");
		nestedEntity1.setNested_attr_String("nestedAttribute1");
		entity1.setAttr_NestedEntity(nestedEntity1);
		
		SampleDomain entity2 = new SampleDomain();
		SampleNestedDomain nestedEntity2 = new SampleNestedDomain();
		entity2.setId(3L);
		entity2.setAttr_String("searchString1");
		nestedEntity2.setNested_attr_String("nestedAttribute2");
		entity2.setAttr_NestedEntity(nestedEntity2);
			
		mongo.insert(entity1, "sample_domain");
		mongo.insert(entity2, "sample_domain");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_domain/_search?fn=lookup&where=sample_domain.attr_String.eq('searchString')&projection.mapsTo=code:id,label:attr_NestedEntity.nested_attr_String");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("nestedAttribute1", values.get(0).getLabel());
		
		CommandMessage cmdMsg1 = build(PLATFORM_ROOT+"/sample_domain/_search?fn=lookup&projection.mapsTo=code:id,label:attr_NestedEntity.nested_attr_String");
		
		MultiOutput multiOp1 = this.commandGateway.execute(cmdMsg1);
		List<Output<?>> ops1  = multiOp1.getOutputs();
		
		assertNotNull(ops1);
		
		List<ParamValue> values1 = (List<ParamValue>)ops1.get(0).getValue();
		
		assertNotNull(values1);
		assertEquals(2, values1.size());
		
		assertEquals("nestedAttribute1", values1.get(0).getLabel());
		assertEquals("nestedAttribute2", values1.get(1).getLabel());
	
		
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/sample_domain/_search?fn=lookup&projection.mapsTo=code:id,label:attr_NestedEntity.nested_attr_String2");
		try {
			MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
			List<Output<?>> ops2  = multiOp2.getOutputs();
		} catch(Exception ex) {
			assertNotNull(ex);
			assertEquals(ex.getCause().getMessage(),"EL1008E: Property or field 'nested_attr_String2' cannot be found on object of type 'com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleNestedDomain' - maybe not public or not valid?");
		}
	}
	
	/**
	 * This test method shows how to sort the Param Values of dynamic lookup in descending order in memory (once mongoDB result is available).
	 * The orderby property expects the value in propertyName.direction pattern.
	 * <p> e.g. if you want to sort the param values in descending order based on <code>label</code> property of <code>ParamValue</code> class, 
	 * you would use below configuration.
	 */
	@Test
	public void test_dynamicLookUpWithProjection_inMemorySorted() {
		mongo.dropCollection("sample_domain");
		
		SampleDomain entity1 = new SampleDomain();
		SampleNestedDomain nestedEntity1 = new SampleNestedDomain();
		entity1.setId(2L);
		entity1.setAttr_String("searchString");
		nestedEntity1.setNested_attr_String("nestedAttribute1");
		entity1.setAttr_NestedEntity(nestedEntity1);
		
		SampleDomain entity2 = new SampleDomain();
		SampleNestedDomain nestedEntity2 = new SampleNestedDomain();
		entity2.setId(3L);
		entity2.setAttr_String("searchString1");
		nestedEntity2.setNested_attr_String("nestedAttribute2");
		entity2.setAttr_NestedEntity(nestedEntity2);
			
		mongo.insert(entity1, "sample_domain");
		mongo.insert(entity2, "sample_domain");
		
		CommandMessage cmdMsg1 = build(PLATFORM_ROOT+"/sample_domain/_search?fn=lookup&projection.mapsTo=code:id,label:attr_NestedEntity.nested_attr_String&orderby=label.desc()");
		
		MultiOutput multiOp1 = this.commandGateway.execute(cmdMsg1);
		List<Output<?>> ops1  = multiOp1.getOutputs();
		
		assertNotNull(ops1);
		
		List<ParamValue> values1 = (List<ParamValue>)ops1.get(0).getValue();
		
		assertNotNull(values1);
		assertEquals(2, values1.size());
		
		assertEquals("nestedAttribute2", values1.get(0).getLabel());
		assertEquals("nestedAttribute1", values1.get(1).getLabel());
	
	}
	
	/**
	 * This test method shows how to sort dynamic lookup param values in MongoDB.
	 * The orderby property expects the value in domainAlias.propertyName.direction pattern.
	 * <p> e.g. if you want to sort the sample_domain based param values in descending order based on <code>nested_attr_String</code> nested property of <code>sample_domain</code> class, 
	 * you would use below configuration.
	 */
	@Test
	public void test_dynamicLookUpWithProjection_dbSorted() {
		mongo.dropCollection("sample_domain");
		
		SampleDomain entity1 = new SampleDomain();
		SampleNestedDomain nestedEntity1 = new SampleNestedDomain();
		entity1.setId(2L);
		entity1.setAttr_String("searchString");
		nestedEntity1.setNested_attr_String("nestedAttribute1");
		entity1.setAttr_NestedEntity(nestedEntity1);
		
		SampleDomain entity2 = new SampleDomain();
		SampleNestedDomain nestedEntity2 = new SampleNestedDomain();
		entity2.setId(3L);
		entity2.setAttr_String("searchString1");
		nestedEntity2.setNested_attr_String("nestedAttribute2");
		entity2.setAttr_NestedEntity(nestedEntity2);
			
		mongo.insert(entity1, "sample_domain");
		mongo.insert(entity2, "sample_domain");
		
		CommandMessage cmdMsg1 = build(PLATFORM_ROOT+"/sample_domain/_search?fn=lookup&projection.mapsTo=code:id,label:attr_NestedEntity.nested_attr_String&orderby=sample_domain.attr_NestedEntity.nested_attr_String.desc()");
		
		MultiOutput multiOp1 = this.commandGateway.execute(cmdMsg1);
		List<Output<?>> ops1  = multiOp1.getOutputs();
		
		assertNotNull(ops1);
		
		List<ParamValue> values1 = (List<ParamValue>)ops1.get(0).getValue();
		
		assertNotNull(values1);
		assertEquals(2, values1.size());
		
		assertEquals("nestedAttribute2", values1.get(0).getLabel());
		assertEquals("nestedAttribute1", values1.get(1).getLabel());
	
	}
	//orderby=sampletask.taskName.asc()
	
	@Test
	public void t6_testSearchByQueryWithCountAggregation() {
		this.mongoOps.dropCollection("staticCodeValue");
		StaticCodeValue scv = new StaticCodeValue("/status", null);
		scv.setId(new Random().nextLong());
		StaticCodeValue scv2 = new StaticCodeValue("/status", null);
		scv2.setId(new Random().nextLong());
		this.mongoOps.insert(scv, "staticCodeValue");
		this.mongoOps.insert(scv2, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&aggregate=count");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		Long count = (Long) multiOp.getSingleResult();
		
		assertNotNull(count);
		assertEquals(Long.valueOf("2"), count);
	}
	
	// TODO - the in-memory flapdoodle mongo does not support the graphLookup query hence @Ignore
	@Ignore
	public void t9_getAllQueuesForUserByAggregation() {
		Stream.of(COLLECTIONS).forEach((collection) -> mongoOps.dropCollection(collection));
		createUsers();
		createUserGroups();
		createQueues();

		String userQueuesQuery = "{ \"aggregate\": \"queue\", \"pipeline\": [ { $graphLookup: { from: \"clientuser\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"loginId\", as: \"users\", restrictSearchWithMatch: { \"loginId\": \"casemanager\" } } }, { $match: { \"users\": { $ne: [] } } } ] }";
		String userGroupQueuesQuery = "{ \"aggregate\": \"queue\", \"pipeline\": [{ $graphLookup: { from: \"clientusergroup\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"usergroups\", restrictSearchWithMatch: {\"members.userId\":\"casemanager\"} } }, { $match: { \"usergroups\" :{ $ne: []} } } ] }";
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/queue/_search?fn=query&where="+userQueuesQuery);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Queue> userQueues = (List<Queue>) multiOp.getSingleResult();
		
		Assert.notEmpty(userQueues, "values cannot be empty");
		
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/queue/_search?fn=query&where="+userGroupQueuesQuery);
		
		MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
		List<Queue> userGrpQueues = (List<Queue>) multiOp2.getSingleResult();
		
		Assert.notEmpty(userGrpQueues, "values cannot be empty");
		
	}
	
	// removed the projection.alias support since not the correct solution. below scenario is not used as of now in the app, however need to discuss the correct approach.
	@Test
	public void t11_getMembersFromUserGroupByAggregation() {
		Stream.of(COLLECTIONS).forEach((collection) -> mongoOps.dropCollection(collection));
		createUsers();
		createUserGroups();
		createQueues();
		
		String query = "{" + 
				"    aggregate : \"clientusergroup\"," + 
				"    \"pipeline\" : [ " + 
				"        {" + 
				"			$match: {" + 
				"                \"members.admin\": true" + 
				"                }" + 
				"		 }," + 
				"        {" + 
				"        $project: {" + 
				"            members: {$filter: {" + 
				"                input: \"$members\"," + 
				"                as: \"member\"," + 
				"                cond: {$eq: [\"$$member.admin\", true]}" + 
				"        }}," + 
				"        _class: 1," + 
				"        _id: 0" + 
				"    }}" +  
				"    ] " + 
				"}";
		
		//CommandMessage cmdMsg = build(PLATFORM_ROOT+"/clientusergroup/_search?fn=query&where="+query+"&projection.alias=groupuser");
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/clientusergroup/_search?fn=query&where="+query);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<ClientUserGroup> values = (List<ClientUserGroup>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		assertEquals(1, values.size());
		assertNotNull(values.get(0).getMembers());
		assertEquals(2, values.get(0).getMembers().size());
		
		//System.out.println(values.get(0).getUserId());
		
	}
 	
	@Test
	public void t12_testSearchByQueryPageable() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=5&page=0");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertEquals(5, response.getContent().size());
		
		//String json = converter.convert(values);
		//System.out.println(json);
	}
	
	@Test
	public void t13_testSearchByQueryPageable_NextPage() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=5&page=1");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertEquals(1, response.getContent().size());
	}

	
	@Test
	public void t14_testSearchByQueryPageableWithSort() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=5&page=0&sortBy=attr_String,DESC");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertEquals(5, response.getContent().size());
		
		assertEquals("6", response.getContent().get(0).getAttr_String());
		assertEquals("5", response.getContent().get(1).getAttr_String());
		assertEquals("4", response.getContent().get(2).getAttr_String());
		assertEquals("3", response.getContent().get(3).getAttr_String());
		assertEquals("2", response.getContent().get(4).getAttr_String());
	}
	
	@Test
	public void t15_testSearchByQueryPageableWithSort_NextPage() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=5&page=1&sortBy=attr_String,DESC");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertEquals(1, response.getContent().size());
		
		assertEquals("1", response.getContent().get(0).getAttr_String());
	}
	
	@Test
	public void t16_testSearchByQueryPageable_SimulatePageNavigation() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		/* page 1 request */
		getFirstPage();
		
		/* page 2 request (Next) */
		getNextPage();
		
		/* page 1 request (Back) */
		getFirstPage();
	}
	
	@Test
	public void t17_testSearchByExamplePageable() {
		cleanInsertSampleCoreAccess(new String[] {"1","1","1","1","1","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=0");
		cmdMsg.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> responsePage = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(responsePage);
		assertNotNull(responsePage.getContent());
		assertEquals(4, responsePage.getContent().size());
	}
	
	@Test
	public void t18_testSearchByExamplePageable_SimulatePageNavigation() {
		cleanInsertSampleCoreAccess(new String[] {"1","1","1","1","1","6"});
		
		/* page 1 request */
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=0");
		cmdMsg.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> responsePage = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(responsePage);
		assertNotNull(responsePage.getContent());
		assertEquals(4, responsePage.getContent().size());
		
		/* page 2 request */
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=1");
		cmdMsg2.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
		List<Output<?>> ops2  = multiOp2.getOutputs();
		
		assertNotNull(ops2);
		
		PageRequestAndRespone<SampleCoreEntityAccess> responsePage2 = (PageRequestAndRespone<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
		assertNotNull(responsePage2);
		assertNotNull(responsePage2.getContent());
		assertEquals(1, responsePage2.getContent().size());
	}
	
	@Test
	public void t19_testSearchByExamplePageableWithSort() {
		cleanInsertSampleCoreAccess(new String[] {"1","1","1","1","1","6"}, "1","2","3","4","5","6");
		
		/* page 1 request */
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=0&sortBy=attr_String2,desc&sortBy=attr_String,desc");
		cmdMsg.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> responsePage = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(responsePage);
		assertNotNull(responsePage.getContent());
		assertEquals(4, responsePage.getContent().size());
		assertEquals("5", responsePage.getContent().get(0).getAttr_String2());
		assertEquals("4", responsePage.getContent().get(1).getAttr_String2());
		assertEquals("3", responsePage.getContent().get(2).getAttr_String2());
		assertEquals("2", responsePage.getContent().get(3).getAttr_String2());
		
		
		/* page 2 request */
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=1&sortBy=attr_String2,desc&sortBy=attr_String,desc");
		cmdMsg2.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
		List<Output<?>> ops2  = multiOp2.getOutputs();
		
		assertNotNull(ops2);
		
		PageRequestAndRespone<SampleCoreEntityAccess> responsePage2 = (PageRequestAndRespone<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
		assertNotNull(responsePage2);
		assertNotNull(responsePage2.getContent());
		assertEquals(1, responsePage2.getContent().size());
		assertEquals("1", responsePage2.getContent().get(0).getAttr_String2());
	}
	
	@Test
	public void t20_tt() {
		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
		scea.setId(1L);
		scea.setAttr_String("test1_string1");
		scea.setAttr_String2("test2_string2");
		scea.setAttr_LocalDate1(LocalDate.now());
		scea.setAttr_LocalDateTime1(LocalDateTime.now());
		scea.setAttr_ZonedDateTime1(ZonedDateTime.now());
		scea.setAttr_Date1(new Date());
		
		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
		scea2.setId(2L);
		scea2.setAttr_String("test1_string1");
		scea2.setAttr_String2("test2_string2");
		scea2.setAttr_LocalDate1(LocalDate.now().plusYears(1));
		scea2.setAttr_LocalDateTime1(LocalDateTime.now().plusHours(2));
		scea2.setAttr_ZonedDateTime1(ZonedDateTime.now().plusHours(9));
		Date dt = new Date();
		dt.setTime(15000);
		scea2.setAttr_Date1(dt);
		
		mongoOps.save(scea, "sample_core_access");
		mongoOps.save(scea2, "sample_core_access");
		
		LocalDateTime inputDate = LocalDateTime.now();
		String inputDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(inputDate);
		LocalDate result = LocalDate.parse(inputDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		
		int year = result.getYear();
	    int month = result.getMonthValue();
	    int day = result.getDayOfMonth();
	    
	    LocalDate nextDate = result.plusDays(1);
	    int nextYear = nextDate.getYear();
	    int nextMonth = nextDate.getMonthValue();
	    int nextDay = nextDate.getDayOfMonth();
		
		// localdate, localdatetime, Date query:
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&where=sample_core_access.attr_LocalDateTime1.goe(java.time.LocalDate.of("+year+", "+month+", "+day+")).and(sample_core_access.attr_LocalDateTime1.lt(java.time.LocalDate.of("+nextYear+", "+nextMonth+", "+nextDay+")))");
		//CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&where=sample_core_access.attr_LocalDateTime1.goe('"+date+"').and(sample_core_access.attr_LocalDateTime1.lt('"+nextDate+"'))");
		
		//zoneddatetime: ?
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
	}

	private void getFirstPage() {
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=4&page=0");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response = (PageRequestAndRespone<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertEquals(4, response.getContent().size());
		assertEquals("1", response.getContent().get(0).getAttr_String());
		assertEquals("2", response.getContent().get(1).getAttr_String());
		assertEquals("3", response.getContent().get(2).getAttr_String());
		assertEquals("4", response.getContent().get(3).getAttr_String());
	}
	
	private void getNextPage() {
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=4&page=1");
		
		MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
		List<Output<?>> ops2  = multiOp2.getOutputs();
		
		assertNotNull(ops2);
		
		PageRequestAndRespone<SampleCoreEntityAccess> response2 = (PageRequestAndRespone<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
		assertNotNull(response2);
		assertNotNull(response2.getContent());
		assertEquals(2, response2.getContent().size());
		assertEquals("5", response2.getContent().get(0).getAttr_String());
		assertEquals("6", response2.getContent().get(1).getAttr_String());
	}

	private void cleanInsertSampleCoreAccess(String[] attr_String, String... attr_String2) {
		mongoOps.dropCollection("sample_core_access");
		
		for(int i=0; i < attr_String.length; i++) {
			SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
			scea.setId(new Random().nextLong());
			scea.setAttr_String(attr_String[i]);
			if(attr_String2 != null && attr_String2.length > 0 && i <= attr_String2.length) {
				scea.setAttr_String2(attr_String2[i]);
			}
			mongo.insert(scea, "sample_core_access");
		}
		
//		for(String attr: attr_String) {
//			SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
//			scea.setAttr_String(attr);
//			mongo.insert(scea, "sample_core_access");
//		}
//		
//		if(attr_String2 != null) {
//			for(String attr2: attr_String2) {
//				SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
//				scea.setAttr_String2(attr2);
//				mongo.insert(scea, "sample_core_access");
//			}
//		}
		
//		SampleCoreEntityAccess scea = new SampleCoreEntityAccess();
//		scea.setAttr_String("1");
//		SampleCoreEntityAccess scea2 = new SampleCoreEntityAccess();
//		scea2.setAttr_String("2");
//		SampleCoreEntityAccess scea3 = new SampleCoreEntityAccess();
//		scea3.setAttr_String("3");
//		SampleCoreEntityAccess scea4 = new SampleCoreEntityAccess();
//		scea4.setAttr_String("4");
//		SampleCoreEntityAccess scea5 = new SampleCoreEntityAccess();
//		scea5.setAttr_String("5");
//		SampleCoreEntityAccess scea6 = new SampleCoreEntityAccess();
//		scea6.setAttr_String("6");
//		
//		mongo.insert(scea, "sample_core_access");
//		mongo.insert(scea2, "sample_core_access");
//		mongo.insert(scea3, "sample_core_access");
//		mongo.insert(scea4, "sample_core_access");
//		mongo.insert(scea5, "sample_core_access");
//		mongo.insert(scea6, "sample_core_access");
	}
	
	
	private Client insertClient() {
		mongoOps.dropCollection("client");
		
		Client c = new Client();
		c.setId(new Random().nextLong());
		c.setName("client");
		c.setCode("c");
		mongoOps.insert(c, "client");
		
		Client c1 = new Client();
		c1.setId(new Random().nextLong());
		c1.setName("client1");
		c1.setCode("c1");
		mongoOps.insert(c1, "client");
		return c;
	}
	
	private void createUsers() {
		mongoOps.dropCollection("clientuser");
		
		ClientUser clientUser = new ClientUser();
		clientUser.setId(new Random().nextLong());
		clientUser.setDisplayName("U1");
		clientUser.setLoginId("casemanager");
		
		
		ClientUser clientUser2 = new ClientUser();
		clientUser2.setId(new Random().nextLong());
		clientUser2.setDisplayName("U2");
		
		ClientUser clientUser3 = new ClientUser();
		clientUser3.setId(new Random().nextLong());
		clientUser3.setDisplayName("U3");
		
		ClientUser clientUser4 = new ClientUser();
		clientUser4.setId(new Random().nextLong());
		clientUser4.setDisplayName("U4");
		
		mongoOps.save(clientUser, "clientuser");
		mongoOps.save(clientUser2, "clientuser");
		mongoOps.save(clientUser3, "clientuser");
		mongoOps.save(clientUser4, "clientuser");
	}
	
	private void createUserGroups() {
		mongoOps.dropCollection("clientusergroup");
		
		ClientUserGroup userGroup = new ClientUserGroup();
		userGroup.setId(new Random().nextLong());
		userGroup.setName("UG1");
		
		List<GroupUser> groupUsers = new ArrayList<>();
		GroupUser groupUser = new GroupUser();
		groupUser.setUserId("casemanager");
		groupUsers.add(groupUser);
		
		GroupUser groupUser22 = new GroupUser();
		groupUser22.setUserId("U3");
		groupUser22.setAdmin(true);
		groupUsers.add(groupUser22);
		
		GroupUser groupUser33 = new GroupUser();
		groupUser33.setUserId("U4");
		groupUser33.setAdmin(true);
		groupUsers.add(groupUser33);
		
		userGroup.setMembers(groupUsers);
		
		ClientUserGroup userGroup2 = new ClientUserGroup();
		userGroup2.setId(new Random().nextLong());
		userGroup2.setName("UG2");
		
		List<GroupUser> groupUsers2 = new ArrayList<>();
		GroupUser groupUser2 = new GroupUser();
		groupUser2.setUserId("U2");
		groupUsers2.add(groupUser2);
		
		userGroup2.setMembers(groupUsers2);
		
		ClientUserGroup userGroup3 = new ClientUserGroup();
		userGroup3.setId(new Random().nextLong());
		userGroup3.setName("UG3");
		
		List<GroupUser> groupUsers3 = new ArrayList<>();
		GroupUser groupUser3 = new GroupUser();
		groupUser3.setUserId("casemanager");
		groupUsers3.add(groupUser3);
		
		userGroup3.setMembers(groupUsers3);
		
		mongoOps.save(userGroup, "clientusergroup");
		mongoOps.save(userGroup2, "clientusergroup");
		mongoOps.save(userGroup3, "clientusergroup");
	}
	
	private void createQueues() {
		mongoOps.dropCollection("queue");
		
		Queue queue = new Queue();
		queue.setId(new Random().nextLong());
		queue.setName("Q1");
		
		ClientUser cu = mongoOps.findOne(new Query(Criteria.where("displayName").is("U1")), ClientUser.class, "clientuser");
		queue.setName(cu.getLoginId());
		queue.setEntityId(cu.getId());
		
		Queue queue2 = new Queue();
		queue2.setId(new Random().nextLong());
		queue2.setName("Q2");
		
		ClientUserGroup cug = mongoOps.findOne(new Query(Criteria.where("name").is("UG1")), ClientUserGroup.class, "clientusergroup");
		queue2.setName(cug.getName());
		queue2.setEntityId(cug.getId());
		
		Queue queue3 = new Queue();
		queue3.setId(new Random().nextLong());
		queue3.setName("Q3");
		
		ClientUserGroup cug2 = mongoOps.findOne(new Query(Criteria.where("name").is("UG1")), ClientUserGroup.class, "clientusergroup");
		queue3.setName(cug2.getName());
		queue3.setEntityId(cug2.getId());
		
		Queue queue4 = new Queue();
		queue4.setId(new Random().nextLong());
		queue4.setName("Q4");
		
		ClientUserGroup cug3 = mongoOps.findOne(new Query(Criteria.where("name").is("UG2")), ClientUserGroup.class, "clientusergroup");
		queue4.setName(cug3.getName());
		queue4.setEntityId(cug3.getId());
		
		Queue queue5 = new Queue();
		queue5.setId(new Random().nextLong());
		queue5.setName("Q5");
		
		
		ClientUser cu2 = mongoOps.findOne(new Query(Criteria.where("displayName").is("U2")), ClientUser.class, "clientuser");
		queue5.setName(cu2.getLoginId());
		queue5.setEntityId(cu2.getId());
		
		mongoOps.save(queue, "queue");
		mongoOps.save(queue2, "queue");
		mongoOps.save(queue3, "queue");
		mongoOps.save(queue4, "queue");
		mongoOps.save(queue5, "queue");
	}
	
	private ClientUserGroup insertClientUserGroup() {
		mongoOps.dropCollection("clientusergroup");
		ClientUserGroup cug = new ClientUserGroup(); 
		cug.setId(new Random().nextLong());
		List<GroupUser> guList = new ArrayList<GroupUser>(); 
		GroupUser gu = new GroupUser(); 
		gu.setUserId("test1"); 
		gu.setAdmin(false);  
		GroupUser gu2 = new GroupUser(); 
		gu2.setUserId("test2"); 
		gu2.setAdmin(false);  
		guList.add(gu);
		guList.add(gu2);
		
		cug.setMembers(guList);
		cug.setName("TTTT");
		
		mongoOps.save(cug, "clientusergroup");
		
		return cug;
	}
	
	private CommandMessage build(String uri) {
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		cmd.setAction(Action._search);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		return cmdMsg;
	}
}

//@Configuration
//class MongoConfiguration extends AbstractMongoConfiguration {
// 
//    @Override
//    protected String getDatabaseName() {
//        return "test";
//    }
// 
//    @Override
//    public Mongo mongo() throws Exception {
//        return new MongoClient("127.0.0.1", 27017);
//    }
// 
//    @Override
//    protected String getMappingBasePackage() {
//        return "com.anthem.nimbus.platform.client.extension.cm";
//    }
//}