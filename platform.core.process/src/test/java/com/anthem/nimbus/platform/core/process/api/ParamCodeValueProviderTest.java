package com.anthem.nimbus.platform.core.process.api;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.internal.RepoBasedConverter;
import com.anthem.oss.nimbus.core.entity.StaticCodeValue;
import com.anthem.oss.nimbus.core.entity.VStaticCodeValue;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.queue.MGroupMember;
import com.anthem.oss.nimbus.core.entity.queue.MUser;
import com.anthem.oss.nimbus.core.entity.queue.MUserGroup;
import com.anthem.oss.nimbus.core.entity.queue.Queue;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;
import com.anthem.oss.nimbus.core.entity.user.GroupUser;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ContextConfiguration(classes = MongoConfiguration.class)
public class ParamCodeValueProviderTest extends AbstractFrameworkIntegrationTests {

	private static final String[] COLLECTIONS = {"queue","clientusergroup","clientuser"};
	
	@Autowired
	MongoOperations mongoOps;
	
	@Autowired
	RepoBasedConverter converter;
	
	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	@Test
	@SuppressWarnings("unchecked")
	public void t1_testSearchByLookupStaticCodeValue() {
		
		this.mongoOps.dropCollection("staticCodeValue");
		final List<ParamValue> expectedValues = new ArrayList<>();
		expectedValues.add(new ParamValue("code1", "label1", "desc1"));
		final StaticCodeValue expected = new StaticCodeValue("/status", expectedValues);
		this.mongoOps.insert(expected, "staticCodeValue");
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/status')");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		final List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue(); // TODO having to cast the output, is that correct ??
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
		
		assertEquals("Anticardiolpin Antibodies", this.converter.serialize("ACL"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void t2_testSearchByLookupModel() {
		insertClient();
		CommandMessage cmdMsg = build("Acme/fep/icr/p/client/_search?fn=lookup&projection.mapsTo=code:name,label:name");
		
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<ParamValue> values = lookupFunctionHandler.execute(exContext, null);
		
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
		CommandMessage cmdMsg = build("Acme/fep/icr/p/client/_search?fn=example");
//		ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
//		List<?> values = (List<?>)exampleFunctionHandler.execute(exContext, null);
		
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
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/client/_search?fn=example");
		cmdMsg.setRawPayload("{\"code\":\""+c.getCode()+"\"}");
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<?> values = (List<?>)exampleFunctionHandler.execute(exContext, null);
		
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
		CommandMessage cmdMsg = build("Acme/fep/icr/p/client/_search?fn=query&where=client.code.eq('c1')");
		
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
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&projection.alias=vstaticCodeValue");
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<?> values = (List<?>)queryFunctionHandler.execute(exContext, null);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		@SuppressWarnings("unchecked")
		List<VStaticCodeValue> values = (List<VStaticCodeValue>) multiOp.getSingleResult();
		
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("/status", values.get(0).getParamCode());
	}
	
	// TODO - 2017/09/06 Tony (AF42192) - Test needs to be updated per new framework changes.
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	public void t51_testSearchByQueryWithProjectionAndMapsTo() {
		
		ClientUserGroup cug = insertClientUserGroup();
	
		CommandMessage cmdMsg = build("Acme/fep/icr/p/clientusergroup/members/_search?fn=query&where=clientusergroup.id.eq('"+cug.getId()+"')");
		
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
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&aggregate=count");
//		ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
//		Holder<Long> count = (Holder<Long>)queryFunctionHandler.execute(exContext, null);
		
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
		
		CommandMessage cmdMsg = build("Acme/fep/cmapp/p/queue/_search?fn=query&where="+finalCriteria);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Queue> values = (List<Queue>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		
	}
	
	// TODO - the in-memory flapdoodle mongo does not support the graphLookup query hence @Ignore
	@Ignore
	public void t9t1_getAllQueuesForUserByNamedQueryAggregation() {
		Stream.of(COLLECTIONS).forEach((collection) -> mongoOps.dropCollection(collection));
		createUsers();
		createUserGroups();
		createQueues();
		
		CommandMessage cmdMsg = build("Acme/fep/cmapp/p/queue/_search?fn=query&where=userQueues");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Queue> values = (List<Queue>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		org.junit.Assert.assertEquals(4, values.size());
		
	}
	
	@Test
	public void t9t2_getMembersFromUserGroupByAggregation() {
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
		
		CommandMessage cmdMsg = build("Acme/fep/cmapp/p/clientusergroup/_search?fn=query&where="+query+"&projection.alias=groupuser");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<GroupUser> values = (List<GroupUser>) multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		org.junit.Assert.assertEquals(2, values.size());
		
		System.out.println(values.get(0).getUserId());
		
	}
 	
	@Test
	@SuppressWarnings({ "unchecked" })
	public void tc9_orderby_desc() {
		inserClientUserRole();
		
		CommandMessage cmdMsg = build("Acme/fep/p/userrole/_search?fn=query&where=userrole.status.eq('Active')&orderby=userrole.name.desc()");

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ClientUserRole> values = (List<ClientUserRole>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals((values.get(0)).getName(), "sandeep");
		assertEquals(values.get(1).getName(), "mantha");
		assertEquals(values.get(2).getName(), "jayant");
	}
	
	@Test
	public void tc10_orderby_desc() {
		ClientUserRole clientUserRole = new ClientUserRole();
		clientUserRole.setName("sandeep");
		clientUserRole.setDescription("desc1");;
		mongoOps.insert(clientUserRole);
		
		ClientUserRole clientUserRole2 = new ClientUserRole();
		clientUserRole2.setName("mantha");
		clientUserRole2.setDescription("desc2");;
		mongoOps.insert(clientUserRole2);
		
		ClientUserRole clientUserRole3 = new ClientUserRole();
		clientUserRole3.setName("rakesh");
		clientUserRole3.setDescription("esc2");;
		mongoOps.insert(clientUserRole3);
		
		ClientUserRole clientUserRole4 = new ClientUserRole();
		clientUserRole4.setName("jayant");
		clientUserRole4.setDescription("dsc2");;
		mongoOps.insert(clientUserRole4);	
	}
	
	@Test
	@SuppressWarnings({ "unchecked" })
	public void tc11_orderby_asc() {
		inserClientUserRole();
		
		CommandMessage cmdMsg = build("Acme/fep/p/userrole/_search?fn=query&where=userrole.status.eq('Active')&orderby=userrole.name.asc()");

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ClientUserRole> values = (List<ClientUserRole>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals((values.get(0)).getName(), "jayant");
		assertEquals(values.get(1).getName(), "mantha");
		assertEquals(values.get(2).getName(), "sandeep");
	}
	
	@Test
	@SuppressWarnings({ "unchecked" })
	public void tc12_orderbywithprojection_asc() {
		inserClientUserRole();
		
		CommandMessage cmdMsg = build("Acme/fep/p/userrole/_search?fn=query&where=userrole.status.eq('Active')&orderby=userrole.name.asc()");

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ClientUserRole> values = (List<ClientUserRole>)ops.get(0).getValue();
		
		assertNotNull(values);
		assertEquals((values.get(0)).getName(), "jayant");
		assertEquals(values.get(1).getName(), "mantha");
		assertEquals(values.get(2).getName(), "sandeep");
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
	
	private void inserClientUserRole() {
		mongoOps.dropCollection("userrole");
		
		ClientUserRole clientUserRole = new ClientUserRole();
		clientUserRole.setName("sandeep");
		clientUserRole.setStatus("Active");
		clientUserRole.setDescription("desc1");;
		mongoOps.insert(clientUserRole,"userrole");
		
		ClientUserRole clientUserRole2 = new ClientUserRole();
		clientUserRole2.setName("mantha");
		clientUserRole2.setStatus("Active");
		clientUserRole2.setDescription("desc2");;
		mongoOps.insert(clientUserRole2,"userrole");
		
		ClientUserRole clientUserRole3 = new ClientUserRole();
		clientUserRole3.setName("rakesh");
		clientUserRole3.setStatus("Inactive");
		clientUserRole3.setDescription("esc2");;
		mongoOps.insert(clientUserRole3,"userrole");
		
		ClientUserRole clientUserRole4 = new ClientUserRole();
		clientUserRole4.setName("jayant");
		clientUserRole4.setStatus("Active");
		clientUserRole4.setDescription("dsc2");;
		mongoOps.insert(clientUserRole4,"userrole");
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