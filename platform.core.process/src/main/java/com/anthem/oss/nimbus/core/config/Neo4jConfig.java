/**
 * 
 */
package com.anthem.oss.nimbus.core.config;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.AccessEntityRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.PlatformUserRepository;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;


/**
 * @author Soham Chakravarti
 *
 */
@Configuration
@EnableNeo4jRepositories(basePackageClasses= {PlatformUserRepository.class,AccessEntityRepository.class}, considerNestedRepositories=true)
@EnableTransactionManagement
@ConfigurationProperties(exceptionIfInvalid=true, prefix="neo4j")
public class Neo4jConfig extends Neo4jConfiguration {

	//@Value("${neo4jUrl:http://neo4j:password@localhost:7474}")
	private String url;
	
	//@Value("${neo4jDriver:org.neo4j.ogm.drivers.http.driver.HttpDriver}")
	private String driver;
	
	@Bean
	@Override 
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(),
				AbstractEntity.class.getPackage().getName()
		);
	}
	
	@Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	@Override
    public Session getSession() throws Exception {
        return super.getSession();
    }
	
	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
	   org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
	   config
	       .driverConfiguration()
	       .setDriverClassName(driver)
	       .setURI(url);
	   return config;
	}
}
