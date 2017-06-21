package com.anthem.oss.nimbus.core.domain.command.execution.search;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ProjectCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings("unchecked")
public class DefaultSearchFunctionHandlerQuery<T, R> extends DefaultSearchFunctionHandler<T, R> {

	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		ModelConfig<?> mConfig = getRootDomainConfig(executionContext);
		
		QuerySearchCriteria querySearchCriteria = createSearchCriteria(executionContext, mConfig);
		Class<?> criteriaClass = mConfig.getReferredClass();
		String alias = findRepoAlias(criteriaClass);
		
		ModelRepository rep = getRepFactory().get(mConfig.getRepo());
		
		return (R)rep._search(criteriaClass, alias, querySearchCriteria);
	}
	
	
	@Override
	protected QuerySearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		QuerySearchCriteria querySearchCriteria = new QuerySearchCriteria();
		
		querySearchCriteria.validate(executionContext);
		
		querySearchCriteria.setWhere(executionContext.getCommandMessage().getCommand().getFirstParameterValue("where"));
		
		querySearchCriteria.setOrderby(executionContext.getCommandMessage().getCommand().getFirstParameterValue("orderby"));
		
		String aggregateAs = cmd.getFirstParameterValue("aggregate");
		querySearchCriteria.setAggregateCriteria(aggregateAs);
		
		if(cmd.getRequestParams().get("projection.alias") != null) {
			ProjectCriteria projectCriteria = new ProjectCriteria();
			projectCriteria.setAlias(cmd.getFirstParameterValue("projection.alias"));
			querySearchCriteria.setProjectCriteria(projectCriteria);
		}
		
		return querySearchCriteria;
	}
	
	

}
