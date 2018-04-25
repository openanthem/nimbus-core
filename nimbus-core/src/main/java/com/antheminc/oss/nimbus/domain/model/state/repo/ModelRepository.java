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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.support.Holder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 * TODO - 08/30/2017: Refactor the methods to accept the argument which is generic enough to get the enough information needed e.g. url, alias etc..  
 */
public interface ModelRepository {

	@Getter @RequiredArgsConstructor
	public enum Aggregation {
		COUNT("count", Holder.class);
		
		public final String alias;
		public final Class<?> type;
		
		@SuppressWarnings("unchecked")
		public <T> Class<T> getType() {
			return (Class<T>)type;
		}
		
		public static Aggregation getByAlias(String alias) {
			return Stream.of(Aggregation.values())
				.filter((aggregation) -> StringUtils.equals(aggregation.getAlias(), alias))
				.findFirst()
				.orElse(null);
		}
	}
	
	/*SOHAM: f.w upgrade v0.3: START */
	
	public <T> T _new(ModelConfig<T> mConfig);
	public <T> T _new(ModelConfig<T> mConfig, T newState);
	
	/*SOHAM: f.w upgrade v0.3: END */
	
	// internally used, not exposed as an Action
	default public <ID extends Serializable, T> T _save(String alias, T state) {
		return state;
	}
	
	//Action._get
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias);
	default public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias, String url) {
		return this._get(id, referredClass, alias);
	}
	
	//Action._info
	
	//Action._update: partial update
	public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state);
	
	//Action._replace: complete update
	public void _replace(Param<?> param);
	public void _replace(List<Param<?>> params);
	public <T> T _replace(String alias, T state);
	
	//Action._delete
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias);
		
	public <T> Object _search(Class<T> referredDomainClass, String alias, Supplier<SearchCriteria<?>> criteria);
		

}
