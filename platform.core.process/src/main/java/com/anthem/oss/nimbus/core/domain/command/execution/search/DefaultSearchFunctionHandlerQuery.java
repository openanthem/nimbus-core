package com.anthem.oss.nimbus.core.domain.command.execution.search;

import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository;
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
		
		return (R)rep._search(criteriaClass, alias, querySearchCriteria, executionContext.getCommandMessage().getCommand().getAbsoluteUri());
	}
	
	
	@Override
	protected QuerySearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		QuerySearchCriteria querySearchCriteria = new QuerySearchCriteria();
		
		querySearchCriteria.validate(executionContext);
		
		querySearchCriteria.setWhere(executionContext.getCommandMessage().getCommand().getFirstParameterValue("where"));
		
		querySearchCriteria.setOrderby(executionContext.getCommandMessage().getCommand().getFirstParameterValue("orderby"));
		
		querySearchCriteria.setFetch(executionContext.getCommandMessage().getCommand().getFirstParameterValue("fetch"));
		
		String aggregateAs = cmd.getFirstParameterValue("aggregate");
		querySearchCriteria.setAggregateCriteria(aggregateAs);
		
		String converter = cmd.getFirstParameterValue("converter");
		querySearchCriteria.setResponseConverter(converter);
		
		ProjectCriteria projectCriteria = new ProjectCriteria();
		
		if(cmd.getRequestParams().get("projection.alias") != null) {
			projectCriteria.setAlias(cmd.getFirstParameterValue("projection.alias"));
			querySearchCriteria.setProjectCriteria(projectCriteria);
		}
		
		if(cmd.getRequestParams().get("projection.mapsTo") != null) {
			String projectMapping = cmd.getFirstParameterValue("projection.mapsTo");
			String[] keyValues = StringUtils.split(projectMapping,",");
			
			Stream.of(keyValues).forEach((kvString) -> {
				if(MapUtils.isEmpty(projectCriteria.getMapsTo())){
					projectCriteria.setMapsTo(new HashMap<String, String>());
				}
				String[] kv = StringUtils.split(kvString,":");
				projectCriteria.getMapsTo().put(kv[0], kv[1]);
			});
		}
		
		return querySearchCriteria;
	}
	
	

}
