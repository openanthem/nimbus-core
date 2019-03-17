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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoDBModelRepositoryOptions;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoSearchByExampleOperation;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoSearchByQueryOperation;
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
	
	@Bean(name="default.rep_mongodb")
	public DefaultMongoModelRepository defaultMongoModelRepository(MongoOperations mongoOps, BeanResolverStrategy beanResolver, MongoDBModelRepositoryOptions options){
		return new DefaultMongoModelRepository(mongoOps, beanResolver, options);
	}
	
	@Bean
	public MongoDBModelRepositoryOptions defaultMongoDBModelRepositoryOptions(MongoOperations mongoOps, DomainConfigBuilder domainConfigBuilder) {
		return MongoDBModelRepositoryOptions.builder()
			.addSearchOperation(new MongoSearchByExampleOperation(mongoOps, domainConfigBuilder))
			.addSearchOperation(new MongoSearchByQueryOperation(mongoOps, domainConfigBuilder))
			.build();
	}
}
