/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.entity.SearchCriteria;

import lombok.Getter;

/**
 * @author AC67870
 *
 */
@Getter
public abstract class MongoDBSearch implements DBSearch {

	private final MongoOperations mongoOps;
	
	private final CommandMessageConverter converter;
	
	private final DomainConfigBuilder domainConfigBuilder;
	
	private final BeanResolverStrategy beanResolver;
	
	public MongoDBSearch(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.mongoOps = beanResolver.get(MongoOperations.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	
	public <T> Class<?> findOutputClass(SearchCriteria<T> criteria, Class<?> referredClass) {
		if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
			return getDomainConfigBuilder().getModel(criteria.getProjectCriteria().getAlias()).getReferredClass();
		}
		else if(criteria.getAggregateCriteria() != null ) {
			com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository.Aggregation aggByAlias = 
					com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository.Aggregation.getByAlias(criteria.getAggregateCriteria());
			
			Assert.notNull(aggByAlias, "Aggregation constant not found for the alias: " +criteria.getAggregateCriteria());
			
			return aggByAlias.getType();
		}
		
		return referredClass;
	}



	
}
