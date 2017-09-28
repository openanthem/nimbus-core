package com.anthem.oss.nimbus.core.domain.command.execution.search;

import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
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
		
		QuerySearchCriteria querySearchCriteria = createSearchCriteria(executionContext, mConfig, actionParameter);
		Class<?> criteriaClass = mConfig.getReferredClass();
		
		String alias = findRepoAlias(mConfig);
		
		ModelRepository rep = getRepFactory().get(mConfig.getRepo());
		
		return (R)rep._search(criteriaClass, alias, querySearchCriteria, executionContext.getCommandMessage().getCommand().getAbsoluteUri());
	}
	
	
	@Override
	protected QuerySearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig, Param<T> actionParam) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		QuerySearchCriteria querySearchCriteria = new QuerySearchCriteria();
		
		querySearchCriteria.validate(executionContext);
		
		resolveNamedQueryIfApplicable(executionContext, mConfig, querySearchCriteria, actionParam);
		
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


	private void resolveNamedQueryIfApplicable(ExecutionContext executionContext, ModelConfig<?> mConfig, QuerySearchCriteria querySearchCriteria, Param<T> actionParam) {
		String where = executionContext.getCommandMessage().getCommand().getFirstParameterValue("where");
		
		// find if where is a named query
		Repo repo = mConfig.getRepo();
		Repo.NamedNativeQuery[] namedQueries = repo.namedNativeQueries();
		
		if(namedQueries != null && namedQueries.length > 0) {
			for(Repo.NamedNativeQuery query: namedQueries) {
				if(StringUtils.equalsIgnoreCase(query.name(), where) && query.nativeQueries() != null) {
					int i = 0;
					for(String q: query.nativeQueries()) {
						if(i == 0) {
							where = q;
						}
						else {
							where = where +"~~"+q;
						}
						i++;
					}
				}
			}
		}
		where = getPathVariableResolver().resolve(actionParam, where);
		
		querySearchCriteria.setWhere(where);
	}

}
