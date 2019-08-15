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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import java.sql.Connection;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Soham Chakravarti
 *
 */
@Configuration
public class RdbmsTestConfig {

	@Autowired
    private Environment env;
	
	@Bean("default.datasource")
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(env.getProperty("repo.rdbms.jdbc.url"));
		dataSource.setUsername(env.getProperty("repo.rdbms.jdbc.username"));
		dataSource.setPassword(env.getProperty("repo.rdbms.jdbc.password"));
		
		dataSource.setTransactionIsolation(
				env.getProperty("repo.rdbms.jdbc.connection.isolation", 
						String.valueOf(Connection.TRANSACTION_READ_COMMITTED)));
		dataSource.setMaximumPoolSize(env.getProperty("repo.rdbms.jdbc.connection.max-total", Integer.class));
		
		return dataSource;
	}
	
	@Bean("default.rep_rdbms.emf")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("default.datasource") DataSource ds) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		
		vendorAdapter.setDatabase(env.getProperty("repo.rdbms.hibernate.database.type", Database.class));
		vendorAdapter.setGenerateDdl(env.getProperty("repo.rdbms.hibernate.generate-ddl", Boolean.class, false));
		
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPackagesToScan(env.getProperty("repo.rdbms.hibernate.scan-packages"));
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setDataSource(ds);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", env.getProperty("repo.rdbms.hibernate.database.dialect"));
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("repo.rdbms.hibernate.database.hbm2ddl-auto"));
		
		jpaProperties.put("hibernate.show_sql", env.getProperty("repo.rdbms.hibernate.sql.show"));
		jpaProperties.put("hibernate.format_sql", env.getProperty("repo.rdbms.hibernate.sql.format"));
		jpaProperties.put("hibernate.use_sql_comments", env.getProperty("repo.rdbms.hibernate.sql.comments"));
		jpaProperties.put("hibernate.connection.isolation", env.getProperty("repo.rdbms.hibernate.connection.isolation"));
		jpaProperties.put("hibernate.connection.autoReconnect", env.getProperty("repo.rdbms.hibernate.connection.auto-reconnect"));
		jpaProperties.put("hibernate.connection.autoReconnectForPools",	env.getProperty("repo.rdbms.hibernate.connection.auto-reconnect-pools"));

		jpaProperties.put("hibernate.connection.release_mode", env.getProperty("repo.rdbms.hibernate.connection.release-mode"));
		
		factory.setJpaProperties(jpaProperties);
		return factory;
	}

	@Bean("default.rep_rdbms.txnMgr")
	public PlatformTransactionManager jpaTxnManager(@Qualifier("default.rep_rdbms.emf") EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
}
