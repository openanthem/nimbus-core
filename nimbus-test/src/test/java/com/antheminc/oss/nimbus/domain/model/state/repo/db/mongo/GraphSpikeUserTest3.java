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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.graphLookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.queue.Queue;
import com.antheminc.oss.nimbus.entity.task.AssignmentTask;
import com.antheminc.oss.nimbus.entity.user.ClientUserGroup;
import com.antheminc.oss.nimbus.entity.user.GroupUser;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;

/**
 * @author Rakesh Patel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ContextConfiguration(classes = MongoConfiguration.class)
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
	
	@Test
	public void t3_createTasks() {
		
		AssignmentTask task = new AssignmentTask();
		task.setStatus("Open");
		task.setDueDate(LocalDate.now());
		task.setTaskType("Patient Enrollment 1");
		task.setQueueCode("casemanager");
		
		AssignmentTask task1 = new AssignmentTask();
		task1.setStatus("Open");
		task1.setDueDate(LocalDate.now());
		task1.setTaskType("Patient Enrollment 2");
		task1.setQueueCode("casemanager");
		
		AssignmentTask task2 = new AssignmentTask();
		task2.setStatus("Open");
		task2.setDueDate(LocalDate.now());
		task2.setTaskType("Patient Enrollment 2");
		task2.setQueueCode("UG1");
		
		mongo.save(task, "assignmenttask");
		mongo.save(task1,"assignmenttask");
		mongo.save(task2,"assignmenttask");
		
		
	}
	
	private void createUsers() {
		mongo.dropCollection("clientuser");
		
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
		
		mongo.save(clientUser, "clientuser");
		mongo.save(clientUser2, "clientuser");
		mongo.save(clientUser3, "clientuser");
		mongo.save(clientUser4, "clientuser");
	}
	
	private void createUserGroups() {
		mongo.dropCollection("clientusergroup");
		
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
		
		mongo.save(userGroup, "clientusergroup");
		mongo.save(userGroup2, "clientusergroup");
		mongo.save(userGroup3, "clientusergroup");
	}
	
	private void createQueues() {
		mongo.dropCollection("queue");
		
		Queue queue = new Queue();
		queue.setId("Q1");
		
		ClientUser cu = mongo.findOne(new Query(Criteria.where("id").is("U1")), ClientUser.class, "clientuser");
		queue.setName(cu.getLoginId());
		queue.setEntityId(cu.getLoginId());
		
		Queue queue2 = new Queue();
		queue2.setId("Q2");
		
		
		ClientUserGroup cug = mongo.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		
		queue2.setName(cug.getName());
		queue2.setEntityId(cug.getId());
		
		Queue queue3 = new Queue();
		queue3.setId("Q3");
		
		
		ClientUserGroup cug2 = mongo.findOne(new Query(Criteria.where("id").is("UG1")), ClientUserGroup.class, "clientusergroup");
		queue3.setName(cug2.getName());
		queue3.setEntityId(cug2.getId());
		
		Queue queue4 = new Queue();
		queue4.setId("Q4");
		
		
		ClientUserGroup cug3 = mongo.findOne(new Query(Criteria.where("id").is("UG2")), ClientUserGroup.class, "clientusergroup");
		queue4.setName(cug3.getName());
		queue4.setEntityId(cug3.getId());
		
		Queue queue5 = new Queue();
		queue5.setId("Q5");
		
		
		ClientUser cu2 = mongo.findOne(new Query(Criteria.where("id").is("U2")), ClientUser.class, "clientuser");
		queue5.setName(cu2.getLoginId());
		queue5.setEntityId(cu2.getLoginId());
		
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