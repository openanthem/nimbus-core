package com.antheminc.oss.nimbus.core.domain.command.execution.search;

import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.fn.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.entity.SearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
public abstract class DefaultSearchFunctionHandler<T, R> extends AbstractFunctionHandler<T, R> {

	protected String findRepoAlias(ModelConfig<?> mConfig) {
		String alias = mConfig.getRepo().alias();
		if(StringUtils.isBlank(alias)) {
			alias = mConfig.getAlias();
		}
		return alias;
	}
	
	protected abstract SearchCriteria<?> createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig, Param<T> cmdParam);
	
}
