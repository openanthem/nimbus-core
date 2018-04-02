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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.StateType;

/**
 * @author Soham Chakravarti
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractListPaginatedParam<T> extends DefaultParamState<List<T>> implements ListParam<T> {
	
	@JsonIgnore
	private Page<T> pageDelegate;

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

	@Override
	protected void initStateInternal() {
		// wrap up default initializations
		super.initStateInternal();
		
		// create page
		List<T> content = Optional.ofNullable(getLeafState()).orElse(Collections.emptyList());
		this.pageDelegate = new PageImpl<>(content);
	}

	private final SetStateListener<List<T>, Object> defaultSetStateListener = new SetStateListener<List<T>, Object>() {
		@Override
		public Action postSetState(Action change, List<T> state, String localLockId, ExecutionRuntime execRt, ListenerContext<List<T>, Object> ctx) {
			List<T> content = Optional.ofNullable(getLeafState()).orElse(Collections.emptyList());
			pageDelegate = new PageImpl<>(content);
			
			return change;
		}
	};

	@Override
	protected SetStateListener<List<T>, Object> getSetStateListener() {
		return defaultSetStateListener;
	}
	
	@Override
	public void setPage(Page<T> page) {
		List<T> content = page.getContent();
		setState(content, null);
		
		pageDelegate = page;
	}
	
	@Override
	public int getNumber() {
		return pageDelegate.getNumber();
	}

	@Override
	public int getSize() {
		return pageDelegate.getSize();
	}

	@Override
	public int getNumberOfElements() {
		return pageDelegate.getNumberOfElements();
	}

	@JsonIgnore 
	@Override
	public List<T> getContent() {
		return pageDelegate.getContent();
	}

	@Override
	public boolean hasContent() {
		return pageDelegate.hasContent();
	}

	@Override
	public Sort getSort() {
		return pageDelegate.getSort();
	}

	@Override
	public boolean isFirst() {
		return pageDelegate.isFirst();
	}

	@Override
	public boolean isLast() {
		return pageDelegate.isLast();
	}

	@Override
	public boolean hasNext() {
		return pageDelegate.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return pageDelegate.hasPrevious();
	}

	@Override
	public Pageable nextPageable() {
		return pageDelegate.nextPageable();
	}

	@Override
	public Pageable previousPageable() {
		return pageDelegate.previousPageable();
	}

	@Override
	public Iterator<T> iterator() {
		return pageDelegate.iterator();
	}

	@Override
	public int getTotalPages() {
		return pageDelegate.getTotalPages();
	}

	@Override
	public long getTotalElements() {
		return pageDelegate.getTotalElements();
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		return pageDelegate.map(converter);
	}
	
	
}
