package com.anthem.oss.nimbus.core.domain.command.execution.search;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.fn.AbstractFunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.entity.SearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
public abstract class DefaultSearchFunctionHandler<T, R> extends AbstractFunctionHandler<T, R> {

	protected String findRepoAlias(Class<?> criteriaClass) {
		String alias = AnnotationUtils.findAnnotation(criteriaClass, Repo.class).alias();
		if(StringUtils.isBlank(alias)) {
			alias = AnnotationUtils.findAnnotation(criteriaClass, Domain.class).value();
		}
		return alias;
	}
	
	protected abstract SearchCriteria<?> createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig);
	
}
