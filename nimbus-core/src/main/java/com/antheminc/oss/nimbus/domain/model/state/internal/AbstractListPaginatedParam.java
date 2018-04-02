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
	
	@Override
	protected List<T> preSetState(List<T> state) {
		// TODO Auto-generated method stub
		return super.preSetState(state);
	}
	
	@Override
	protected void postSetState(Action change, List<T> state) {
		// create only if not invoked via setPage
		List<T> content = Optional.ofNullable(getLeafState()).orElse(Collections.emptyList());
		this.pageDelegate = new PageImpl<>(content);
	}
	
	@Override
	public void setPage(Page<T> page) {
		List<T> content = page.getContent();
		setState(content);
	}
	
	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfElements() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Pageable nextPageable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable previousPageable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalPages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalElements() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
