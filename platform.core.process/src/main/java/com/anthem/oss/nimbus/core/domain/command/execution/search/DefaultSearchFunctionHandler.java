/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.core.domain.command.execution.search;

import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.fn.AbstractFunctionHandler;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.SearchCriteria;

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
