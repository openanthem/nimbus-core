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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.List;
import java.util.Objects;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedListModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultListModelState<T, M> extends DefaultListModelState<T> implements MappedListModel<T, M> {
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ListModel<M> mapsTo; 
	
	public MappedDefaultListModelState(ListModel<M> mapsTo, ListParam<T> associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		super(associatedParam, config, provider, elemCreator);
		
		Objects.requireNonNull(mapsTo, "MapsTo model must not be null.");
		this.mapsTo = mapsTo;
		
		//createElemParamMapping();
	}
	
	@JsonIgnore
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public MappedDefaultListModelState<T, M> findIfMapped() {
		return this;
	}
	
	
	protected void createElemParamMapping() {
		if(getMapsTo().size()==0)
			return;
	
		instantiateOrGet();
		
		getMapsTo().getParams().stream()
			.map(Param::findIfCollectionElem)
			.forEach(elem->{
				String mapsToElemId = elem.getElemId();
				
				Param<?> mappedElem = getElemCreator().apply(this, mapsToElemId);
				this.templateParams().add(mappedElem);
			});
	}
	
}
