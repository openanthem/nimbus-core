package com.anthem.oss.nimbus.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.graphLookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.queue.Queue;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;
import com.anthem.oss.nimbus.core.entity.user.GroupUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * @author Rakesh Patel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraphSpikeUserTest3 extends AbstractFrameworkIntegrationTests{
	
	private static final String[] COLLECTIONS = {"queue","clientusergroup","clientuser"};
	
	@Test
	public void t1_createEntitiesWithEmbeddedEdge() {
//		Stream.of(COLLECTIONS).forEach((collection) -> mongo.dropCollection(collection));
//		
//		createUsers();
//		createUserGroups();
//		createQueues();
		
	}
	
	@Ignore
	public void t2_getDirectQueuesForAUser() {
		AggregationOperation matchOperation = matchCriteria("_id", "U1");
		
		AggregationOperation graphOperation = graphLookup("queue")
				.startWith("$_id")
				.connectFrom("_id")
				.connectTo("entityId")
				.as("userqueues");
		
		List<AggregationOperation> aggregateOps = new ArrayList<>();
		aggregateOps.add(matchOperation);
		aggregateOps.add(graphOperation);
		
		AggregationResults<String> models = mongo.aggregate(Aggregation.newAggregation(aggregateOps), "clientuser", String.class);
		
		assertNotNull(models);
		assertNotNull(models.getMappedResults());
		assertThat(models.getMappedResults().get(0)).isNotBlank();
		//System.out.println("USER -> QUEUE: "+models.getMappedResults().get(0)+"\n");
	}
	
	//TODO in memory mongo driver does not support $graphLookup
	@SuppressWarnings({ "unchecked"})
	@Ignore
	public void t3_getGroupQueuesForAUser_NativeQuery() {
		String query2 = "{\n" + 
				"\"aggregate\":\"clientuser\",\n" + 
				"\"pipeline\":	[\n" + 
				"		{\n" + 
				"			$match: {\n" + 
				"			   \"_id\": \"U2\"\n" + 
				"			\n" + 
				"			}\n" + 
				"		},\n" + 
				"    {\n" + 
				"			$graphLookup: {\n" + 
				"			    from: \"clientusergroup\",\n" + 
				"			    startWith: \"$_id\",\n" + 
				"			    connectFromField: \"_id\",\n" + 
				"			    connectToField: \"members.userId\",\n" + 
				"			    as: \"usergroups\"\n" + 
				"			}\n" + 
				"		},\n" + 
				"    {\n" + 
				"			$graphLookup: {\n" + 
				"			    from: \"queue\",\n" + 
				"			    startWith: \"$usergroups._id\",\n" + 
				"			    connectFromField: \"usergroups\",\n" + 
				"			    connectToField: \"entityId\",\n" + 
				"			    as: \"queues\"\n" + 
				"			}\n" + 
				"		},\n" + 
				"    {\n" + 
				"			$project: {\n" + 
				"			    \"queues\": 1,\n" + 
				"			    \"_id\": 0\n" + 
				"			}\n" + 
				"		}\n" +
				"	]\n" + 
				"}";
		
		CommandResult users = mongo.executeCommand(query2);
		com.mongodb.BasicDBList firstOut = (com.mongodb.BasicDBList)users.get("result");
		
		List<Queue> queueList = new ArrayList<>();
		
		for(Object dbo: firstOut) {
			queueList.addAll(mongo.getConverter().read(List.class,(BasicDBList)((BasicDBObject)dbo).get("queues")));
		}
		
		assertThat(queueList).isNotEmpty();
		assertThat(queueList).hasAtLeastOneElementOfType(Queue.class);

	}
	
	private void createUsers() {
		mongo.dropCollection("clientuser");
		
		ClientUser clientUser = new ClientUser();
		clientUser.setId("U1");
		clientUser.setDisplayName("testClientUserDisplayName_1");
		
		
		ClientUser clientUser2 = new ClientUser();
		clientUser2.setId("U2");
		clientUser2.setDisplayName("testClientUserDisplayName_2");
		
		mongo.save(clientUser, "clientuser");
		mongo.save(clientUser2, "clientuser");
		
	}
	
	private void createUserGroups() {
		mongo.dropCollection("clientusergroup");
		
		ClientUserGroup userGroup = new ClientUserGroup();
		userGroup.setId("UG1");
		userGroup.setName("testClientUserGroupName_1");
		
		List<GroupUser> groupUsers = new ArrayList<>();
		GroupUser groupUser = new GroupUser();
		groupUser.setUserId("U2");
		groupUsers.add(groupUser);
		
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
		groupUser3.setUserId("U1");
		groupUsers3.add(groupUser3);
		
		userGroup3.setMembers(groupUsers3);
		
		mongo.save(userGroup, "clientusergroup");
		mongo.save(userGroup2, "clientusergroup");
		mongo.save(userGroup3, "clientusergroup");
		
	}
	
	private void createQueues() {
		mongo.dropCollection("queue");
		
		Queue queue = new Queue();
		queue.setId("Q1");
		queue.setName("testQueue_1");
		
		ClientUser cu = mongo.findOne(new Query(Criteria.where("id").is("U1")), ClientUser.class, "clientuser");
		
		queue.setEntityId(cu.getId());
		
		
		Queue queue2 = new Queue();
		queue2.setId("Q2");
		queue2.setName("testQueue_2");
		
		ClientUserGroup cug = mongo.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		
		queue2.setEntityId(cug.getId());
		
		Queue queue3 = new Queue();
		queue3.setId("Q3");
		queue3.setName("testQueue_3");
		
		ClientUserGroup cug2 = mongo.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		
		queue3.setEntityId(cug2.getId());
		
		Queue queue4 = new Queue();
		queue4.setId("Q4");
		queue4.setName("testQueue_4");
		
		ClientUserGroup cug3 = mongo.findOne(new Query(Criteria.where("id").is("UG2")), ClientUserGroup.class, "clientusergroup");
		
		queue4.setEntityId(cug3.getId());
		
		Queue queue5 = new Queue();
		queue5.setId("Q5");
		queue5.setName("testQueue_5");
		
		ClientUser cu2 = mongo.findOne(new Query(Criteria.where("id").is("U2")), ClientUser.class, "clientuser");
		
		queue5.setEntityId(cu2.getId());
		
		mongo.save(queue, "queue");
		mongo.save(queue2, "queue");
		mongo.save(queue3, "queue");
		mongo.save(queue4, "queue");
		mongo.save(queue5, "queue");
		
	}
	
	private AggregationOperation matchCriteria(String key, String value) {
		return match(Criteria.where(key).is(value));
	}

	
}