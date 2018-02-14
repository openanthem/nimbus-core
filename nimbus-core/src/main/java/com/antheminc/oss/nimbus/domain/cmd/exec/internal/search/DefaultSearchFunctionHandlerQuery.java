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

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
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
		
		String where = resolveNamedQueryIfApplicable(executionContext, mConfig, actionParam);
		querySearchCriteria.setWhere(where);
		
		querySearchCriteria.setOrderby(cmd.getFirstParameterValue(Constants.SEARCH_REQ_ORDERBY_MARKER.code));
		querySearchCriteria.setFetch(cmd.getFirstParameterValue(Constants.SEARCH_REQ_FETCH_MARKER.code));
		querySearchCriteria.setAggregateCriteria(cmd.getFirstParameterValue(Constants.SEARCH_REQ_AGGREGATE_MARKER.code));
		
		querySearchCriteria.setProjectCriteria(buildProjectCritera(cmd));
		querySearchCriteria.setPaginationCriteria(buildPageCriteria(cmd));
		
		
		
		return querySearchCriteria;
	}

}
