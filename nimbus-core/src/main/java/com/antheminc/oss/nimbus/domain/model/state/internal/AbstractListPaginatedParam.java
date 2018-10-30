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
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractListPaginatedParam<T> extends DefaultParamState<List<T>> implements ListParam<T> {
	
	@Getter @Setter
	@JsonIgnore
	private Pageable pageable;
	
	@Getter @Setter
	@JsonIgnore
	private Supplier<Long> totalCountSupplier;
	
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
	public Page<T> getPage() {
		return pageable == null ? PageWrapper.getPage(this, null, () -> Long.class.cast(size())) : PageWrapper.getPage(this, pageable, totalCountSupplier);
	}
	
	@Override
	public void setPage(List<T> content, Pageable pageable, Supplier<Long> totalCountSupplier) {
		changeStateTemplate((rt, h, lockId)->{

			setState(content);

			this.pageable = pageable;
			this.totalCountSupplier = totalCountSupplier;
			
			return null;
		});
	}
	
	public void clearPageMeta() {
		//this.pageable = null;
		//this.totalCountSupplier = null;
	}
	
	@Getter
	public static class PageWrapper<T> extends PageImpl<T> {
		
		public PageWrapper(ListParam<T> p) {
			super(new ReadOnlyListSupplier<>(p));
		}
		
		public PageWrapper(ListParam<T> p, Pageable pageable, long total) {
			super(new ReadOnlyListSupplier<>(p), pageable, total);
		}
		
		@JsonIgnore
		@Override
		public List<T> getContent() {
			return super.getContent();
		}
		
		@RequiredArgsConstructor @Getter
		public static class PageRequestAndRespone<T> {
			
			private final List<T> content;
			private final Pageable pageable;
			private final Supplier<Long> totalSupplier;
		}
		
		public static <T> Page<T> getPage(ListParam<T> p, Pageable pageable, Supplier<Long> totalSupplier) {

			Assert.notNull(totalSupplier, "TotalSupplier must not be null!");

			if (pageable == null || pageable.getOffset() == 0) {

				if (pageable == null) {
					return new PageWrapper<T>(p);
				} else if(pageable.getPageSize() > p.size()) {
					return new PageWrapper<T>(p, pageable, p.size());
				} else {
					return new PageWrapper<T>(p, pageable, totalSupplier.get());
				}
			}

			if (p.size() != 0 && pageable.getPageSize() > p.size()) {
				return new PageWrapper<T>(p, pageable, pageable.getOffset() + p.size());
			}

			return new PageWrapper<T>(p, pageable, totalSupplier.get());
		}
	}
}
