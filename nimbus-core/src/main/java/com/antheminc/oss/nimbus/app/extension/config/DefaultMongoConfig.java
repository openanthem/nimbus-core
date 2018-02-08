/**
 * 
 */
package com.antheminc.oss.nimbus.app.extension.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.CustomConversions;

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
@EnableMongoAuditing
public class DefaultMongoConfig {

	@Bean
	public CustomConversions defaultMongoCustomConversions() {
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
