/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedListElemParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultListElemParamState<E, M> extends DefaultListElemParamState<E> implements MappedListElemParam<E, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Param<M> mapsTo;
	
	@JsonIgnore final private Notification.Consumer<M> delegate;
	
	public static class MappedLeafElemState<E, M> extends MappedDefaultListElemParamState<E, M> implements LeafParam<E> {
		private static final long serialVersionUID = 1L;
		
		public MappedLeafElemState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, String elemId) {
			super(parentModel, config, provider, elemId);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, String elemId) {
		this(parentModel, config, provider, (ListElemParam<M>)findOrCreateMapsTo(parentModel, elemId));
	}
	
	private MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, ListElemParam<M> mapsTo) {
		super(parentModel, config, provider, mapsTo.getElemId());
		
		// check if @Path is configured to refer to nested element model's param
		if(config.findIfMapped()!=null //TODO Soham: Added this check to handle mapped noConversion scenario during EntityState build  
				&& MapsTo.hasCollectionPath(config.findIfMapped().getPath())) {
			Param<M> mapsToNestedParam = mapsTo.findParamByPath(config.findIfMapped().getPath().colElemPath());
			this.mapsTo = mapsToNestedParam;
		} else {
			this.mapsTo = mapsTo;
		} 
		
		this.delegate = new InternalNotificationConsumer<>(this);
		
		getMapsTo().registerConsumer(this);
	}
	
	public static <E> ListElemParam<?> findOrCreateMapsTo(ListModel<E> parentModel, String elemId) {
		MappedListModel<E, ?> mappedParentModel = parentModel.findIfMapped();
		
		//check if mapsToElem already exists
		ListModel<?> mapsToParentModel = mappedParentModel.getMapsTo();
		
		return mapsToParentModel.getLockTemplate().execute(()->{
			Param<?> maps2Param = mapsToParentModel.templateParams().find(elemId);
			final ListElemParam<?> mapsToElem;
			
			if(maps2Param!=null) 
				mapsToElem = maps2Param.findIfCollectionElem();
			else 
				mapsToElem = mapsToParentModel.add();
			
			return mapsToElem;
		});
	}
}
