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
package com.antheminc.oss.nimbus.test.scenarios.multidb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.antheminc.oss.nimbus.app.extension.config.DefaultMongoConfig;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.extension.ChangeLogCommandEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoDBModelRepositoryOptions;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo.DefaultMongoModelRepository;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.config.IMongodConfig;
import lombok.RequiredArgsConstructor;

/**
 * @author Tony Lopez
 *
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MongoPropertiesTest.class)
public class MultipleMongoConfig extends DefaultMongoConfig {

	private final MongoPropertiesTest nimbusMongoProperties;
	
	@Autowired
	private IMongodConfig embeddedMongoConfig;
	
	@Primary
	@Bean
	public MongoDbFactory factoryPrimary(MongoProperties mongoProperties) {
//		return useCustomConnections(mongoProperties);
		return useEmbeddedConnection(mongoProperties);
	}

	@Bean
	public MongoDbFactory factorySecondary(MongoProperties mongoProperties) {
//		return useCustomConnections(mongoProperties);
		return useEmbeddedConnection(mongoProperties);
	}
	
	@Primary
	@Bean("default.rep_dbPrimary")
	public DefaultMongoModelRepository mongoModelRepositoryPrimary(
			@Qualifier("default.MongoOperationsPrimary") MongoOperations mongoOps, BeanResolverStrategy beanResolver,
			MongoDBModelRepositoryOptions options) {
		return new DefaultMongoModelRepository(mongoOps, beanResolver, options);
	}

	@Bean("default.rep_dbSecondary")
	public DefaultMongoModelRepository mongoModelRepositorySecondary(
			@Qualifier("default.MongoOperationsSecondary") MongoOperations mongoOps,
			BeanResolverStrategy beanResolver, MongoDBModelRepositoryOptions options) {
		return new DefaultMongoModelRepository(mongoOps, beanResolver, options);
	}

	@Primary
	@Bean({ "default.MongoOperations", "default.MongoOperationsPrimary" })
	public MongoTemplate mongoTemplatePrimary(MongoCustomConversions customConversions) throws Exception {
		MongoDbFactory factory = factoryPrimary(this.nimbusMongoProperties.getPrimary());
		MappingMongoConverter mongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				new MongoMappingContext());
		mongoConverter.setCustomConversions(customConversions);
		return new MongoTemplate(factory, mongoConverter);
	}

	@Bean("default.MongoOperationsSecondary")
	public MongoTemplate mongoTemplateSecondary(MongoCustomConversions customConversions) throws Exception {
		MongoDbFactory factory = factorySecondary(this.nimbusMongoProperties.getSecondary());
		MappingMongoConverter mongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				new MongoMappingContext());
		mongoConverter.setCustomConversions(customConversions);
		return new MongoTemplate(factory, mongoConverter);
	}

	/**
	 * <p>Sample code for building a MongoDB factory with multiple connections
	 * @param mongoProperties the database connection properties
	 * @return the MongoDB factory instance
	 */
	@SuppressWarnings("unused")
	private MongoDbFactory useCustomConnections(MongoProperties mongoProperties) {
		return new SimpleMongoDbFactory(new MongoClient(mongoProperties.getHost(), mongoProperties.getPort()),
				mongoProperties.getDatabase());
	}

	/**
	 * <p>Build a MongoDB factory for connecting to the embedded mongo
	 * instance. <p>Intended for testing purposes.
	 * @param databaseName the db to connect to
	 * @return the MongoDB factory instance
	 */
	private MongoDbFactory useEmbeddedConnection(MongoProperties mongoProperties) {
		return new SimpleMongoDbFactory(new MongoClient(embeddedMongoConfig.net().getBindIp(), embeddedMongoConfig.net().getPort()), mongoProperties.getDatabase());
	}
	
	@Bean
	@DependsOn("default.rep_dbPrimary")
	@Override
	public ChangeLogCommandEventHandler changeLogCommandEventHandler(BeanResolverStrategy beanResolver, ModelRepositoryFactory modelRepositoryFactory) {
		ModelRepository modelRepository = modelRepositoryFactory.get(Repo.Database.rep_custom, "rep_dbPrimary");
		return new ChangeLogCommandEventHandler(beanResolver, modelRepository);
	}
}