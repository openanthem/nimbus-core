/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.QuadScopedEventListener;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.entity.process.ProcessFlow;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class QuadModel<V, C> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ExecutionEntity<V, C>.ExModel root;
	
	@JsonIgnore transient private final Model<C> core;
	
	@JsonIgnore transient private final Model<V> view;
	
	@JsonIgnore transient private QuadScopedEventListener eventPublisher;
	
	public QuadModel(ExecutionEntity<V, C>.ExModel root) {
		this.root = root;
		
		this.core = findChildModel(getRoot(), "/c");
		this.view = findChildModel(getRoot(), "/v");
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Model<T> findChildModel(Model<?> parent, String beanPath) {
		return (Model<T>)parent.getParams().stream()
			.filter(p->StringUtils.equals(beanPath, p.getBeanPath()))
			.map(p->p.findIfNested())
			.findFirst()
			.orElse(null);
	}
	
	public Model<?> getView() {
		if(view==null)
			return core;
		
		return view;
	}
	
	
	public ProcessFlow getFlow() {
		return getRoot().getState().getFlow();
	}
	
	@Override
	protected void finalize() throws Throwable {
		getRoot().getExecutionRuntime().stop();
		
		super.finalize();
	}
		
}
