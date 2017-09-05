package com.anthem.nimbus.platform.core.process.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.internal.RepoBasedCodeToDescriptionConverter;
import com.anthem.oss.nimbus.core.entity.StaticCodeValue;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.queue.MGroupMember;
import com.anthem.oss.nimbus.core.entity.queue.MUser;
import com.anthem.oss.nimbus.core.entity.queue.MUserGroup;
import com.anthem.oss.nimbus.core.entity.queue.Queue;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;
import com.anthem.oss.nimbus.core.entity.user.GroupUser;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamCodeValueProviderTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	MongoOperations mongoOps;
	
	@Autowired
	RepoBasedCodeToDescriptionConverter converter;
	
	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	@Test
	@SuppressWarnings("unchecked")
	public void t1_testSearchByLookupStaticCodeValue() {
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/status')");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue(); // TODO having to cast the output, is that correct ??
		assertNotEquals(0, values.size());
		
		values.forEach((v)->System.out.println(v.getCode()));
	}
	
	@Test
	public void t11_testSearchByLookupStaticCodeValueElemMatch() {
		
		assertEquals("Anticardiolpin Antibodies",converter.serialize("ACL"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void t1_testUpdateStaticCodeValue() {
		
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_update");
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Output<?>> ops  = multiOp.getOutputs();
		
		assertNotNull(ops);
		
		List<ParamValue> values = (List<ParamValue>)ops.get(0).getValue(); // TODO having to cast the output, is that correct ??
		assertNotEquals(0, values.size());
		
		values.forEach((v)->System.out.println(v.getCode()));
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
		Client c = insertClient();
		
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
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&projection.alias=vstaticCodeValue");
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<?> values = (List<?>)queryFunctionHandler.execute(exContext, null);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<?> values = (List<?>)multiOp.getSingleResult();
		
		assertNotNull(values);
		assertEquals(1, values.size());
	}
	
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void t6_testSearchByQueryWithCountAggregation() {
		CommandMessage cmdMsg = build("Acme/fep/icr/p/staticCodeValue/_search?fn=query&where=staticCodeValue.paramCode.eq('/status')&aggregate=count");
//		ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
//		Holder<Long> count = (Holder<Long>)queryFunctionHandler.execute(exContext, null);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		Holder<Long> count = (Holder<Long>)multiOp.getSingleResult();
		
		assertNotNull(count);
		assertEquals(Long.valueOf("1"), count.getState());
	}
	
	@Test
	public void t7_testSearchByQueryAssociation() {
		insertUserAndQueue();
		
		String associationString = getAssociationString();
		
		CommandMessage cmdMsg = build("Acme/fep/cmapp/p/queue/_search?fn=query&where="+associationString);
		
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<?> values = (List<?>)queryFunctionHandler.execute(exContext, null);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<?> values = (List<?>)multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		values.forEach(System.out::println);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void t8_testSearchByQueryAssociationWithCountAggregation() {
		insertUserAndQueue();
		
		String associationString = getAssociationString();
		
		CommandMessage cmdMsg = build("Acme/fep/cmapp/p/queue/_search?fn=query&where="+associationString+"&aggregate=count");
		
		//ExecutionContext exContext = new ExecutionContext(cmdMsg, null);
		//List<Holder<Integer>> values = (List<Holder<Integer>>)queryFunctionHandler.execute(exContext, null);
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<Holder<Integer>> values = (List<Holder<Integer>>)multiOp.getSingleResult();
		
		Assert.notEmpty(values, "values cannot be empty");
		values.forEach(System.out::println);
		
		assertEquals(Integer.valueOf("1"), values.get(0).getState());
	}

	private void createStaticCodeValues() {
		ParamValue pv = new ParamValue(null,"OPEN","Open");
		ParamValue pv1 = new ParamValue(null, "ACTIVE", "Active");
		
		List<ParamValue> pvs = new ArrayList<>();
		pvs.add(pv);
		pvs.add(pv1);
		
		StaticCodeValue scv = new StaticCodeValue("status", pvs);
		mongoOps.insert(scv, "staticCodeValue");
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
		
//		final String whereClause = "qClientUserRole.description.startsWith('d')";
//		final String orderByClause = "qClientUserRole.name.desc()";
//		final Binding binding = new Binding();
//		QClientUserRole qClientUserRole = new QClientUserRole("a");
//        binding.setProperty("qClientUserRole", qClientUserRole);
//        final GroovyShell shell = new GroovyShell(binding); 
//        Predicate predicate = (Predicate)shell.evaluate(whereClause); 
//		OrderSpecifier orderBy = (OrderSpecifier)shell.evaluate(orderByClause); 
//        assertNotNull("Not Null", predicate);
//        SpringDataMongodbQuery<ClientUserRole> query = new SpringDataMongodbQuery<>(mongoOps, ClientUserRole.class);
//		List<ClientUserRole> list = query.where(predicate).orderBy(orderBy).fetch();
//		assertEquals(list.get(0).getName(), "sandeep");
//		assertEquals(list.get(1).getName(), "mantha");
//		assertEquals(list.get(2).getName(), "jayant");
		
	}
	
	@Test
	@SuppressWarnings({ "unchecked" })
	public void tc10_orderby_asc() {
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
	public void tc10_orderbywithprojection_asc() {
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
	
	private void inserClientUserRole() {
		mongoOps.dropCollection("userrole");
		
		ClientUserRole clientUserRole = new ClientUserRole();
		clientUserRole.setName("sandeep");
		//clientUserRole.setStatus(Status.ACTIVE);
		clientUserRole.setDescription("desc1");;
		mongoOps.insert(clientUserRole,"userrole");
		
		ClientUserRole clientUserRole2 = new ClientUserRole();
		clientUserRole2.setName("mantha");
		//clientUserRole2.setStatus(Status.ACTIVE);
		clientUserRole2.setDescription("desc2");;
		mongoOps.insert(clientUserRole2,"userrole");
		
		ClientUserRole clientUserRole3 = new ClientUserRole();
		clientUserRole3.setName("rakesh");
		//clientUserRole3.setStatus(Status.INACTIVE);
		clientUserRole3.setDescription("esc2");;
		mongoOps.insert(clientUserRole3,"userrole");
		
		ClientUserRole clientUserRole4 = new ClientUserRole();
		clientUserRole4.setName("jayant");
		///clientUserRole4.setStatus(Status.ACTIVE);
		clientUserRole4.setDescription("dsc2");;
		mongoOps.insert(clientUserRole4,"userrole");
	}
	private void insertUserAndQueue() {
		mongoOps.dropCollection("user");
		mongoOps.dropCollection("queue");
		mongoOps.dropCollection("usergroup");
		mongoOps.dropCollection("groupmember");
		
		Queue queue = new Queue();
		//queue.setCode("test1");
		queue.setName("test queue");
		
		MUser user = new MUser();
		user.setName("user1");
		user.setCode("1");
		mongoOps.insert(user,"user");
		
		MGroupMember grpMember = new MGroupMember();
		grpMember.setName("adminMembers");
		grpMember.setAdmin(true);
		grpMember.setUserName("user2");
		mongoOps.insert(grpMember, "groupmember");
		
		MUserGroup ug = new MUserGroup();
		ug.setName("group1");
		ug.addGroupMembers(grpMember);
		mongoOps.insert(ug,"usergroup");
		
		//queue.addUserGroups(ug);
		//queue.addUsers(user);
		mongoOps.insert(queue);
		
		Queue queue1 = new Queue();
		//queue1.setCode("test2");
		queue1.setName("test2 queue");
		MUser user1 = new MUser();
		user1.setName("user2");
		user1.setCode("2");
		mongoOps.insert(user1,"user");
		//queue1.addUsers(user1);
		mongoOps.insert(queue1);
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
	
	private String getAssociationString() {
		return "{\n" + 
				"  \"domainAlias\": \"queue\",\n" + 
				"  \"associatedEntities\": [\n" + 
				"    {\n" + 
				"      \"domainAlias\": \"user\",\n" + 
				"      \"associationStartWith\": \"users\",\n" + 
				"      \"associationFrom\": \"users\",\n" + 
				"      \"associationTo\": \"name\",\n" + 
				"      \"criteria\": [\n" + 
				"        {\n" + 
				"          \"key\": \"name\",\n" + 
				"          \"value\": \"user1\"\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"associationAlias\": \"queueToUsers\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"domainAlias\": \"usergroup\",\n" + 
				"      \"associationStartWith\": \"userGroups\",\n" + 
				"      \"associationFrom\": \"userGroups\",\n" + 
				"      \"associationTo\": \"name\",\n" + 
				"      \"criteria\": [],\n" + 
				"      \"associationAlias\": \"queueToGroups\",\n" + 
				"      \"unwind\": true,\n" + 
				"      \"associatedEntities\": [\n" + 
				"        {\n" + 
				"          \"domainAlias\": \"groupmember\",\n" + 
				"          \"associationStartWith\": \"queueToGroups.groupMembers\",\n" + 
				"          \"associationFrom\": \"queueToGroups\",\n" + 
				"          \"associationTo\": \"name\",\n" + 
				"          \"criteria\": [\n" + 
				"            {\n" + 
				"              \"key\": \"userName\",\n" + 
				"              \"value\": \"user1\"\n" + 
				"            }\n" + 
				"          ],\n" + 
				"          \"associationAlias\": \"groupToMember\"\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"criteria\": []\n" +
				"}";
	}
	
	private CommandMessage build(String uri) {
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		cmd.setAction(Action._search);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		return cmdMsg;
	}
}