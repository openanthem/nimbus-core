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

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DefaultListModelState<T> extends DefaultModelState<List<T>> implements ListModel<T> {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore 
	final private DefaultListElemParamState.Creator<T> elemCreator;
	
	public DefaultListModelState(ListParam<T> associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		super(associatedParam, config, provider);
		this.elemCreator = elemCreator;
	}
	
	@Override
	protected void initStateInternal() {
		List<?> colEntityState = isMapped() ? findIfMapped().getMapsTo().getState() : getState();
		
		if(CollectionUtils.isEmpty(colEntityState))
			return;
		
		for(@SuppressWarnings("unused") Object e : colEntityState.toArray()) {
			add();
		}
//		colEntityState.stream()
//			.forEach(entityElem->add());
		
	}

	@Override
	public List<T> instantiateAndSet() {
		return changeStateTemplate((execRt, h, lockId)->{
			clear();
			
			List<T> newInstance =  super.instantiateAndSet();
			return newInstance;			
		});
	}

	@Override
	public ListElemParam<T> createElement(String elemId) {
		return getElemCreator().apply(this, elemId).findIfCollectionElem();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListParam<T> getAssociatedParam() {
		return super.getAssociatedParam().findIfCollection();
	}
	
	@Override
	public T getState(int i) {
		return (T)getAssociatedParam().getState(i);
	}

	@Override
	public T getLeafState(int i) {
		return getAssociatedParam().getLeafState(i);
	}
	
	@Override
	public String toElemId(int i) {
		return getAssociatedParam().toElemId(i);
	}
	@Override
	public int fromElemId(String elemId) {
		return getAssociatedParam().fromElemId(elemId);
	}

	@Override
	public int size() {
		return getAssociatedParam().size();
	}

	@Override
	public ListElemParam<T> add() {
		return getAssociatedParam().add();
	}
	
	@Override
	public boolean addAll(List<T> elems) {
		return getAssociatedParam().addAll(elems);
	}
	
	@Override
	public boolean add(T elem) {
		return getAssociatedParam().add(elem);
	}
	
	@Override
	public boolean add(ListElemParam<T> pColElem) {
		return getAssociatedParam().add(pColElem);
	}
	
	@Override
	public boolean remove(ListElemParam<T> pColElem) {
		return getAssociatedParam().remove(pColElem);
	}
	
	@Override
	public void clear() {
		getAssociatedParam().clear();
	}
	
	@Override
	public boolean contains(Param<?> other) {
		return getAssociatedParam().contains(other);
	}
	
	@JsonIgnore
	@Override
	public ParamConfig<T> getElemConfig() {
		StateType.NestedCollection<T> typeSAC = getAssociatedParam().getType().findIfCollection(); 
		ParamConfigType.NestedCollection<T> typeConfig = typeSAC.getConfig().findIfCollection();
		
		ParamConfig<T> elemConfig = typeConfig.getElementConfig();
		return elemConfig;
	}
	
	@Override
	public String getElemConfigId() {
		return getElemConfig().getId();
	}
	
	@Override
	public MappedListModel<T, ?> findIfMapped() {
		return null;
	}
	
	@Override
	public DefaultListModelState<T> findIfListModel() {
		return this;
	}
}
