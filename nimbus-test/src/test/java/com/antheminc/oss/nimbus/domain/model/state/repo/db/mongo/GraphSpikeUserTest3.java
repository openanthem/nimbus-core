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

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import com.antheminc.oss.nimbus.entity.queue.Queue;
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
		
		//CommandResult users = mongo.executeCommand(query2);
		Document users = mongo.executeCommand(query2);
		System.out.println(users);
		//com.mongodb.BasicDBList firstOut = (com.mongodb.BasicDBList)users.get("result");
		
		//List<Queue> queueList = new ArrayList<>();
		
//		for(Object dbo: firstOut) {
//			queueList.addAll(mongo.getConverter().read(List.class,(BasicDBList)((BasicDBObject)dbo).get("queues")));
//		}
		
//		assertThat(queueList).isNotEmpty();
//		assertThat(queueList).hasAtLeastOneElementOfType(Queue.class);
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