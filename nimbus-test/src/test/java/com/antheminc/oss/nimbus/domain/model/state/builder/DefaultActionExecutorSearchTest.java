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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
import com.antheminc.oss.nimbus.entity.StaticCodeValue;
import com.antheminc.oss.nimbus.entity.VStaticCodeValue;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.queue.Queue;
import com.antheminc.oss.nimbus.entity.user.ClientUserGroup;
import com.antheminc.oss.nimbus.entity.user.GroupUser;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.entity.sample.s0.core.SampleCoreEntityAccess;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ContextConfiguration(classes = MongoConfiguration.class)
@SuppressWarnings("unchecked")
public class DefaultActionExecutorSearchTest extends AbstractFrameworkIntegrationTests {

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
		final StaticCodeValue expected = new StaticCodeValue("/status", expectedValues);
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/status')");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue();
		assertEquals(1, values.size());
		assertEquals(expectedValues.get(0).getCode(), values.get(0).getCode());
		assertEquals(expectedValues.get(0).getLabel(), values.get(0).getLabel());
		assertEquals(expectedValues.get(0).getDesc(), values.get(0).getDesc());
	}
	
	@Test
	public void t11_testSearchByLookupStaticCodeValueElemMatch() {
		this.mongoOps.dropCollection("staticCodeValue");
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue("ACL", "Anticardiolpin Antibodies", null));
		final StaticCodeValue expected = new StaticCodeValue("anything", expectedValues);
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
		this.insertClient();
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/client/_search?fn=query&where=client.code.eq('c1')");
		
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
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&projection.alias=vstaticCodeValue");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<VStaticCodeValue> values = (List<VStaticCodeValue>) multiOp.getSingleResult();
		
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("/status", values.get(0).getParamCode());
	}
	
	// TODO - 2017/09/06 Tony (AF42192) - Test needs to be updated per new framework changes.
	@Ignore
	@Test
	public void t51_testSearchByQueryWithProjectionAndMapsTo() {
		ClientUserGroup cug = insertClientUserGroup();
	
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/clientusergroup/members/_search?fn=query&where=clientusergroup.id.eq('"+cug.getId()+"')");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<GroupUser> values = (List<GroupUser>)multiOp.getSingleResult();
		
		assertNotNull(values);
		assertEquals(2, values.size());
	}
	
	@Test
	public void t6_testSearchByQueryWithCountAggregation() {
		this.mongoOps.dropCollection("staticCodeValue");
		this.mongoOps.insert(new StaticCodeValue("/status", null), "staticCodeValue");
		this.mongoOps.insert(new StaticCodeValue("/status", null), "staticCodeValue");
		
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

		String userQueues = "{ \"aggregate\": \"queue\", \"pipeline\": [ { $graphLookup: { from: \"clientuser\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"loginId\", as: \"users\", restrictSearchWithMatch: { \"loginId\": \"casemanager\" } } }, { $match: { \"users\": { $ne: [] } } } ] }";
		String userGroupQueues = "{ \"aggregate\": \"queue\", \"pipeline\": [{ $graphLookup: { from: \"clientusergroup\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"usergroups\", restrictSearchWithMatch: {\"members.userId\":\"casemanager\"} } }, { $match: { \"usergroups\" :{ $ne: []} } } ] }";
		String finalCriteria = userQueues+"~~"+userGroupQueues;
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/queue/_search?fn=query&where="+finalCriteria);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Queue> values = (List<Queue>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		
	}
	
	// TODO - the in-memory flapdoodle mongo does not support the graphLookup query hence @Ignore
	@Ignore
	public void t10_getAllQueuesForUserByNamedQueryAggregation() {
		Stream.of(COLLECTIONS).forEach((collection) -> mongoOps.dropCollection(collection));
		createUsers();
		createUserGroups();
		createQueues();
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/p/queue/_search?fn=query&where=userQueues");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Queue> values = (List<Queue>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		org.junit.Assert.assertEquals(4, values.size());
		
	}
	
	@Test
	public void t11_getMembersFromUserGroupByAggregation() {
		Stream.of(COLLECTIONS).forEach((collection) -> mongoOps.dropCollection(collection));
		createUsers();
		createUserGroups();
		createQueues();
		
		String query = "{\n" + 
				"    \"aggregate\" : \"clientusergroup\",\n" + 
				"    \"pipeline\" : [ \n" + 
				"        {\n" + 
				"			$match: {\n" + 
				"                \"members.admin\": true\n" + 
				"                }\n" + 
				"		},\n" + 
				"        {\n" + 
				"        $project: {\n" + 
				"            groupuser: {$filter: {\n" + 
				"                input: \"$members\",\n" + 
				"                as: \"member\",\n" + 
				"                cond: {$eq: [\"$$member.admin\", true]}\n" + 
				"        }},\n" + 
				"        _id: 0\n" + 
				"    }}\n" +  
				"    ] \n" + 
				"}";
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/clientusergroup/_search?fn=query&where="+query+"&projection.alias=groupuser");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<GroupUser> values = (List<GroupUser>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		assertEquals(2, values.size());
		
		System.out.println(values.get(0).getUserId());
		
	}
 	
	@Test
	public void t12_testSearchByQueryPageable() {
		cleanInsertSampleCoreAccess(new String[] {"1","2","3","4","5","6"});
		
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=5&page=0");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		Page<?> response = (Page<?>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> response = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> response = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> response = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> responsePage = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> responsePage = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
		assertNotNull(responsePage);
		assertNotNull(responsePage.getContent());
		assertEquals(4, responsePage.getContent().size());
		
		/* page 2 request */
		CommandMessage cmdMsg2 = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=example&pageSize=4&page=1");
		cmdMsg2.setRawPayload("{\"attr_String\":\"1\"}");
		
		MultiOutput multiOp2 = this.commandGateway.execute(cmdMsg2);
		List<Output<?>> ops2  = multiOp2.getOutputs();
		
		assertNotNull(ops2);
		
		Page<SampleCoreEntityAccess> responsePage2 = (Page<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> responsePage = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> responsePage2 = (Page<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
		assertNotNull(responsePage2);
		assertNotNull(responsePage2.getContent());
		assertEquals(1, responsePage2.getContent().size());
		assertEquals("1", responsePage2.getContent().get(0).getAttr_String2());
	}

	private void getFirstPage() {
		CommandMessage cmdMsg = build(PLATFORM_ROOT+"/sample_core_access/_search?fn=query&pageSize=4&page=0");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		Page<SampleCoreEntityAccess> response = (Page<SampleCoreEntityAccess>)ops.get(0).getValue();
		
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
		
		Page<SampleCoreEntityAccess> response2 = (Page<SampleCoreEntityAccess>)ops2.get(0).getValue();
		
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
		c.setName("client");
		c.setCode("c");
		mongoOps.insert(c, "client");
		
		Client c1 = new Client();
		c1.setName("client1");
		c1.setCode("c1");
		mongoOps.insert(c1, "client");
		return c;
	}
	
	private void createUsers() {
		mongoOps.dropCollection("clientuser");
		
		ClientUser clientUser = new ClientUser();
		clientUser.setId("U1");
		clientUser.setLoginId("casemanager");
		clientUser.setDisplayName("testClientUserDisplayName_1");
		
		
		ClientUser clientUser2 = new ClientUser();
		clientUser2.setId("U2");
		clientUser2.setDisplayName("testClientUserDisplayName_2");
		
		ClientUser clientUser3 = new ClientUser();
		clientUser3.setId("U3");
		clientUser3.setDisplayName("testClientUserDisplayName_3");
		
		ClientUser clientUser4 = new ClientUser();
		clientUser4.setId("U4");
		clientUser4.setDisplayName("testClientUserDisplayName_4");
		
		mongoOps.save(clientUser, "clientuser");
		mongoOps.save(clientUser2, "clientuser");
		mongoOps.save(clientUser3, "clientuser");
		mongoOps.save(clientUser4, "clientuser");
	}
	
	private void createUserGroups() {
		mongoOps.dropCollection("clientusergroup");
		
		ClientUserGroup userGroup = new ClientUserGroup();
		userGroup.setId("UG1");
		userGroup.setName("testClientUserGroupName_1");
		
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
		userGroup2.setId("UG2");
		userGroup2.setName("testClientUserGroupName_2");
		
		List<GroupUser> groupUsers2 = new ArrayList<>();
		GroupUser groupUser2 = new GroupUser();
		groupUser2.setUserId("U2");
		groupUsers2.add(groupUser2);
		
		userGroup2.setMembers(groupUsers2);
		
		ClientUserGroup userGroup3 = new ClientUserGroup();
		userGroup3.setId("UG3");
		userGroup3.setName("testClientUserGroupName_3");
		
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
		queue.setId("Q1");
		
		ClientUser cu = mongoOps.findOne(new Query(Criteria.where("id").is("U1")), ClientUser.class, "clientuser");
		queue.setName(cu.getLoginId());
		queue.setEntityId(cu.getLoginId());
		
		Queue queue2 = new Queue();
		queue2.setId("Q2");
		
		
		ClientUserGroup cug = mongoOps.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		
		queue2.setName(cug.getName());
		queue2.setEntityId(cug.getId());
		
		Queue queue3 = new Queue();
		queue3.setId("Q3");
		
		
		ClientUserGroup cug2 = mongoOps.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		queue3.setName(cug2.getName());
		queue3.setEntityId(cug2.getId());
		
		Queue queue4 = new Queue();
		queue4.setId("Q4");
		
		
		ClientUserGroup cug3 = mongoOps.findOne(new Query(Criteria.where("id").is("UG2")), ClientUserGroup.class, "clientusergroup");
		queue4.setName(cug3.getName());
		queue4.setEntityId(cug3.getId());
		
		Queue queue5 = new Queue();
		queue5.setId("Q5");
		
		
		ClientUser cu2 = mongoOps.findOne(new Query(Criteria.where("id").is("U2")), ClientUser.class, "clientuser");
		queue5.setName(cu2.getLoginId());
		queue5.setEntityId(cu2.getLoginId());
		
		mongoOps.save(queue, "queue");
		mongoOps.save(queue2, "queue");
		mongoOps.save(queue3, "queue");
		mongoOps.save(queue4, "queue");
		mongoOps.save(queue5, "queue");
	}
	
	private ClientUserGroup insertClientUserGroup() {
		mongoOps.dropCollection("clientusergroup");
		ClientUserGroup cug = new ClientUserGroup(); 
		cug.setId("1"); 
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