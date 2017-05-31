package com.anthem.oss.nimbus.core.domain.command.execution.search;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.entity.SearchCriteria;

import lombok.Data;

/**
 * @author Rakesh Patel
 *
 */
@Data
public abstract class DefaultSearchFunctionHandler<T, R> implements FunctionHandler<T, R> {

	@Autowired DomainConfigBuilder domainConfigBuilder;
	
	@Autowired ModelRepositoryFactory repFactory;
	
	@Autowired CommandMessageConverter converter;
	
	protected ModelConfig<?> getRootDomainConfig(ExecutionContext eCtx) {
		return getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
	}

	protected String findRepoAlias(Class<?> criteriaClass) {
		String alias = AnnotationUtils.findAnnotation(criteriaClass, Repo.class).alias();
		if(StringUtils.isBlank(alias)) {
			alias = AnnotationUtils.findAnnotation(criteriaClass, Domain.class).value();
		}
		return alias;
	}
	
	protected abstract SearchCriteria<?> createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig);
	
}
