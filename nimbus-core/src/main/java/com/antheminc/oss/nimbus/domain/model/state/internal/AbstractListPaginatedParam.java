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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractListPaginatedParam<T> extends DefaultParamState<List<T>> implements ListParam<T> {
	
	@JsonIgnore
	private Pageable pageable;
	
	@JsonIgnore
	private int totalContent;
	
	public AbstractListPaginatedParam(Model<?> parentModel, ParamConfig<List<T>> config, EntityStateAspectHandlers aspectHandlers) {
		super(parentModel, config, aspectHandlers);
	}
	
	@Override
	public StateType.NestedCollection<T> getType() {
		return super.getType().findIfCollection();
	}
	
	@Override
	public abstract MappedListParam<T, ?> findIfMapped();
	
	@Override
	public abstract AbstractListPaginatedParam<T> findIfCollection();

	public Page<T> getPage() {
		return pageable==null ? new PageWrapper<T>(this) : new PageWrapper<T>(this, pageable, totalContent);
	}
	
	@Override
	protected Action postSetState(Action change, List<T> state, String localLockId, ExecutionRuntime execRt, SetStateListener<List<T>> cb) {
		clearPageMeta();
		return super.postSetState(change, state, localLockId, execRt, cb);
	}
	
	@Override
	public void setPage(List<T> content, Pageable pageable, int totalContent) {
		this.pageable = pageable;
		this.totalContent = totalContent;
		setState(content);
	}
	
	protected void clearPageMeta() {
		pageable = null;
		totalContent = 0;
	}

	
	@Getter
	public static class PageWrapper<T> extends PageImpl<T> {
		
		public PageWrapper(ListParam<T> p) {
			super(new ReadOnlyListSupplier<>(p));
		}
		
		public PageWrapper(ListParam<T> p, Pageable pageable, int total) {
			super(new ReadOnlyListSupplier<>(p), pageable, total);
		}
		
		@JsonIgnore
		@Override
		public List<T> getContent() {
			return super.getContent();
		}
	}
}
