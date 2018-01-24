/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.search;

import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.entity.SearchCriteria.ProjectCriteria;
import com.antheminc.oss.nimbus.entity.SearchCriteria.QuerySearchCriteria;

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
