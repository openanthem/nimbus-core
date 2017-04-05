/**
 * 
 */
package com.anthem.oss.nimbus.core.config;


import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Soham Chakravarti
 *
 */
//TODO - Rename core config classes for example DefaultNeo4jConfig
@Configuration
@EnableNeo4jRepositories(basePackages="com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement", transactionManagerRef = "neo4jTransactionManager", considerNestedRepositories=true)
@EnableTransactionManagement
@ConfigurationProperties
@Getter @Setter
public class Neo4jConfig {

	private String neo4jUrl;
	
	private String neo4jDriver;
	
	@Bean
	@ConditionalOnMissingBean
	public org.neo4j.ogm.config.Configuration configuration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
	   config
	       .driverConfiguration()
	       .setDriverClassName(neo4jDriver)
	       .setURI(neo4jUrl);
	   return config;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration) {
		return new SessionFactory(configuration,
				AbstractEntity.class.getPackage().getName()
		);
	}
	
//	@Bean
//    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
//	@Override
//    public Session session() throws Exception {
//        return super.session();
//    }
	
	@Bean
	@ConditionalOnMissingBean
	public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
		return new Neo4jTransactionManager(sessionFactory);
	}

}
