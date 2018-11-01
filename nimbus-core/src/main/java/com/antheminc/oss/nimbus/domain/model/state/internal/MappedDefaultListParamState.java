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

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.antheminc.oss.nimbus.domain.model.state.Notification.ActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultListParamState<T, M> extends DefaultListParamState<T> implements EntityState.MappedListParam<T, M>, NotificationConsumerDelegate<List<M>> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final ListParam<M> mapsTo;
	
	@JsonIgnore private final Notification.Consumer<List<M>> delegate;
	
	public static class LeafState<T, M> extends MappedDefaultListParamState<T, M> implements LeafParam<List<T>> {
		private static final long serialVersionUID = 1L;
		
		public LeafState(ListParam<M> mapsTo, Model<?> parentModel, ParamConfig<List<T>> config, EntityStateAspectHandlers provider) {
			super(mapsTo, parentModel, config, provider);
		}
		
		@JsonIgnore
		@Override
		public boolean isLeaf() {
			return true;
		}
		
		@Override
		public LeafState<T, M> findIfLeaf() {
			return this;
		}
	}
	
	public MappedDefaultListParamState(ListParam<M> mapsTo, Model<?> parentModel, ParamConfig<List<T>> config, EntityStateAspectHandlers provider) {
		super(parentModel, config, provider);
		this.mapsTo = mapsTo;
		
		this.delegate = new InternalNotificationConsumer<List<M>>(this) {
			
			@Override
			protected void onEventNewElem(Notification<List<M>> event) {
				ListModel<?> mapsToListModel = event.getEventParam().findIfCollectionElem().getParentModel();
				
				mapsToListModel.getLockTemplate().execute(()->{
					// check if mapped elem was already created
					String elemId = event.getEventParam().findIfCollectionElem().getElemId();
					Param<?> param = getType().findIfNested().getModel().templateParams().find(elemId);
					
					if(param!=null) {
						MappedParam<?, ?> mappedParam = param.findIfMapped();
						
						logit.trace(()->"[onEventNewElem] found existing mappedParam with path: "+mappedParam.getPath()
							+" which mapsTo: "+mappedParam.getMapsTo().getPath()
							+" -> Returning w/o adding another mappedElem");
						return;
					}
					
					// add new
					ListElemParam<T> mappedElem = add();
					logit.trace(()->"[onEventNewElem] created new mappedElem: "+mappedElem.getPath());			
				});
			}
			
			@Override
			protected void onEventDeleteElem(Notification<List<M>> event) {
				ListModel<?> mapsToListModel = event.getEventParam().findIfCollectionElem().getParentModel();
				
				mapsToListModel.getLockTemplate().execute(()->{
					logit.trace(()->"[onEventDeleteElem] received for mapsTo.ListParamElem: "+event.getEventParam().getPath());
					
					//TODO affectRemoveChange(pElem, execRt, false);
					
					String elemId = event.getEventParam().findIfCollectionElem().getElemId();
					
					ListModel<T> mappedColModel = (ListModel<T>)getType().findIfNested().getModel().findIfListModel();
					Param<?> mappedParamElem = mappedColModel.templateParams().remove(elemId);
					
					emitNotification(new Notification<>(mappedColModel.getAssociatedParam(), ActionType._deleteElem, mappedParamElem));
					
					if(getRootExecution().getExecutionRuntime().isStarted())
						emitEvent(Action._delete, mappedParamElem);
					
					logit.trace(()->"[onEventDeleteElem] removed mapped.ListParamElem: "+mappedParamElem.getPath());
				});	
			}
			
			@Override
			protected void onEventResetModel(Notification<List<M>> event) {
				// ensure model is instantiated
//				super.onEventUpdateState(event);
				
				// synch-up between mapped & mapsTo
				ListModel<?> mapsToListModel = event.getEventParam().findIfCollection().getType().getModel();	
				
				mapsToListModel.getLockTemplate().execute(()->{
					// only scenario to handle here is of "clear" of list model, all other scenarios should have been handled at elem-event level
//					if(mapsToListModel.size() != 0)
//						throw new InvalidStateException("Update state event for List Model can be processed only when state is initialized or reset. "
//								+ "Invalid state found for mappedParam: "+getPath()+" with mapsTo: "+mapsToListModel.getPath());
					
					// clear mapped
					clear(false);
					
					clearPageMeta();
				});
			}
			
			@Override
			protected void onEventUpdateState(Notification<List<M>> event) {
				super.onEventUpdateState(event);
				
				ListParam<M> mapsToCol = event.getEventParam().findIfCollection();
				
				// update page attributes
				setPageable(mapsToCol.getPageable());
				setTotalCountSupplier(mapsToCol.getTotalCountSupplier());
			}
		};

		getMapsTo().registerConsumer(this);
	}
	
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@Override
	public MappedDefaultListParamState<T, M> findIfMapped() {
		return this;
	}
	
	@Override
	public boolean requiresConversion() {
		Class<?> mappedElemClass = getType().findIfCollection().getModel().getElemConfig().getReferredClass();
		Class<?> mapsToElemClass = getMapsTo().getType().findIfCollection().getModel().getElemConfig().getReferredClass();
		
		// conversion required when mappedClass and mapsToClass are NOT same
		return (mappedElemClass!=mapsToElemClass);
	}
}
