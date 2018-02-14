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

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.SearchCriteria;

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
