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
/**
 * 
 */
package com.antheminc.oss.nimbus.app.extension.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.MongoIdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo.DefaultMongoModelPersistenceHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo.DefaultMongoModelRepository;
import com.antheminc.oss.nimbus.support.mongo.MongoConvertersBuilder;

/**
 * @author Soham Chakravrati
 *
 */
@Configuration
@EnableMongoAuditing(dateTimeProviderRef="default.zdt.provider")
public class DefaultMongoConfig {

	@Bean
	public MongoCustomConversions defaultMongoCustomConversions() {
		return new MongoConvertersBuilder().addDefaults().build();
	}
	
	@Bean(name="default.rep_mongodb_handler")
	public DefaultMongoModelPersistenceHandler defaultMongoModelPersistenceHandler(@Qualifier("default.rep_mongodb") ModelRepository rep){
		return new DefaultMongoModelPersistenceHandler(rep);
	}
	
	@Bean(name="default.rep_mongodb")
	public DefaultMongoModelRepository defaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo, BeanResolverStrategy beanResolver){
		return new DefaultMongoModelRepository(mongoOps, idSequenceRepo, beanResolver);
	}
	
	@Bean
	public MongoIdSequenceRepository mongoIdSequenceRepository(MongoOperations mongoOperations){
		return new MongoIdSequenceRepository(mongoOperations);
	}

}
