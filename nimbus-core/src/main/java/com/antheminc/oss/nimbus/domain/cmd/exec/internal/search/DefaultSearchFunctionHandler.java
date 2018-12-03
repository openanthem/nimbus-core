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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ProjectCriteria;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings("unchecked")
@EnableLoggingInterceptor
public abstract class DefaultSearchFunctionHandler<T, R> extends AbstractFunctionHandler<T, R> {

	public static final String PROJECTIONS_SEPARATOR = ",";
	public static final String KEY_VALUE_SEPARATOR = ":";
	
	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		ModelConfig<?> mConfig = getRootDomainConfig(executionContext);
		
		Class<?> criteriaClass = mConfig.getReferredClass();
		String alias = findRepoAlias(mConfig);
		
		ModelRepository rep = getRepFactory().get(mConfig.getRepo());
		
		return (R)rep._search(criteriaClass, alias, () -> this.createSearchCriteria(executionContext, mConfig, actionParameter));
	}
	
	protected abstract SearchCriteria<?> createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig, Param<T> cmdParam);
	
	
	protected String findRepoAlias(ModelConfig<?> mConfig) {
		String alias = mConfig.getRepo().alias();
		if(StringUtils.isBlank(alias)) {
			alias = mConfig.getAlias();
		}
		return alias;
	}
	
	protected Pageable buildPageCriteria(Command cmd) {
		String pageSize = cmd.getFirstParameterValue(Constants.SEARCH_REQ_PAGINATION_SIZE.code);
		String page = cmd.getFirstParameterValue(Constants.SEARCH_REQ_PAGINATION_PAGE_NUM.code);
		String[] sortBy = cmd.getParameterValue(Constants.SEARCH_REQ_PAGINATION_SORT_PROPERTY.code);
		
		if(StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(page)) {
			if(sortBy != null && sortBy.length > 0) {
				List<Order> sortByList = Stream.of(sortBy)
					.map(s -> s.split(PROJECTIONS_SEPARATOR))
					.filter(s -> s.length == 2)
					.map(s -> new Order(Direction.fromString(s[1]), s[0]))
					.collect(Collectors.toList());
				
				Sort sort = new Sort(sortByList);
				
				return new PageRequest(Integer.valueOf(page), Integer.valueOf(pageSize), sort);
			}
			else{
				return new PageRequest(Integer.valueOf(page), Integer.valueOf(pageSize));
			}
		}
		return null;
	}
	
	protected ProjectCriteria buildProjectCriteria(Command cmd) {
		if(cmd.getRequestParams().get(Constants.SEARCH_REQ_PROJECT_ALIAS_MARKER.code) != null) {
			ProjectCriteria projectCriteria = new ProjectCriteria();
			projectCriteria.setAlias(cmd.getFirstParameterValue(Constants.SEARCH_REQ_PROJECT_ALIAS_MARKER.code));
			return projectCriteria;
		}
		
		if(cmd.getRequestParams().get(Constants.SEARCH_REQ_PROJECT_MAPPING_MARKER.code) != null) {
			ProjectCriteria projectCriteria = new ProjectCriteria();
			String projectMapping = cmd.getFirstParameterValue(Constants.SEARCH_REQ_PROJECT_MAPPING_MARKER.code);
			String[] keyValues = StringUtils.split(projectMapping, PROJECTIONS_SEPARATOR);
			
			Stream.of(keyValues).forEach((kvString) -> {
				if(MapUtils.isEmpty(projectCriteria.getMapsTo())){
					projectCriteria.setMapsTo(new HashMap<String, String>());
				}
				String[] kv = StringUtils.split(kvString, KEY_VALUE_SEPARATOR);
				projectCriteria.getMapsTo().put(kv[0], kv[1]);
			});
			return projectCriteria;
		}
		return null;
	}
	
}
