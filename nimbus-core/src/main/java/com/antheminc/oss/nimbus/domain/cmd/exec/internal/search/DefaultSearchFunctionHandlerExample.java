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
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ExampleSearchCriteria;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@EnableLoggingInterceptor
public class DefaultSearchFunctionHandlerExample<T, R> extends DefaultSearchFunctionHandler<T, R> {

	@Override
	protected ExampleSearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig, Param<T> actionParam) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		ExampleSearchCriteria exampleSearchCriteria = new ExampleSearchCriteria<>();
		exampleSearchCriteria.validate(executionContext);
				
		Class<?> criteriaClass = mConfig.getReferredClass();
		T criteria = (T)getConverter().toType(criteriaClass, executionContext.getCommandMessage().getRawPayload());
		
		exampleSearchCriteria.setWhere(criteria);
		exampleSearchCriteria.setAggregateCriteria(cmd.getFirstParameterValue(Constants.SEARCH_REQ_AGGREGATE_MARKER.code));
		
		exampleSearchCriteria.setProjectCriteria(buildProjectCriteria(cmd));
		exampleSearchCriteria.setPageRequest(buildPageCriteria(cmd));
		
		exampleSearchCriteria.setCmd(executionContext.getCommandMessage().getCommand());
		return exampleSearchCriteria;
	}
}
