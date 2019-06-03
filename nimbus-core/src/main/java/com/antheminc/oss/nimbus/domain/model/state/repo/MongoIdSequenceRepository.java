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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.entity.DBSequence;

import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@Getter
public class MongoIdSequenceRepository implements IdSequenceRepository {

	MongoOperations mongoOperations;
	
	public MongoIdSequenceRepository(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	@Override
	public long getNextSequenceId(String key) throws SequenceException {
		//get sequence id
		Query query = new Query(Criteria.where("_id").is(key));
		
		//increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		//return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(true);
		
		DBSequence seqId = getMongoOperations().findAndModify(query, update, options, DBSequence.class, "sequence");

		//if no id, throws SequenceException]
		if (seqId == null) {
			throw new SequenceException("Unable to get sequence id for key : " + key);
		}

		return seqId.getSeq();
	}

}
