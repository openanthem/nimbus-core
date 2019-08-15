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
package com.antheminc.oss.nimbus.app.extension.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.DefaultJpaModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.JpaSearchByQuery;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;

/**
 * @author Soham Chakravarti
 *
 */
@ConditionalOnBean(name="default.rep_rdbms.emf")
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef="default.zdt.provider")
public class DefaultRdbmsConfig implements TransactionManagementConfigurer {

	@Autowired @Qualifier("default.rep_rdbms.txnMgr")
	private PlatformTransactionManager txnManager;
	
	
	// Change with JPA impl for IdSeqRepo
	@Bean(name="default.rep_rdbms")
	public DefaultJpaModelRepository defaultRdbmsRepository(
			@Qualifier("default.rep_rdbms.emf") EntityManagerFactory entityManagerFactory, 
			JavaBeanHandler beanHandler,
			@Qualifier("default.rep_rdbms.query") JpaSearchByQuery dbSearch){
		
		return new DefaultJpaModelRepository(entityManagerFactory, beanHandler, dbSearch);
	}
	
	
	@Bean(name="default.rep_rdbms.query")
	public JpaSearchByQuery defaultJpaDbSearch(BeanResolverStrategy beanResolver) {
		return new JpaSearchByQuery(beanResolver);
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return txnManager;
	}
}
