/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
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
					
					String elemId = event.getEventParam().findIfCollectionElem().getElemId();
					
					ListModel<T> mappedColModel = (ListModel<T>)getType().findIfNested().getModel().findIfListModel();
					Param<?> mappedParamElem = mappedColModel.templateParams().remove(elemId);
					
					logit.trace(()->"[onEventDeleteElem] removed mapped.ListParamElem: "+mappedParamElem.getPath());
				});	
			}
			
			@Override
			protected void onEventResetModel(Notification<List<M>> event) {
				// ensure model is instantiated
				super.onEventUpdateState(event);
				
				// synch-up between mapped & mapsTo
				ListModel<?> mapsToListModel = event.getEventParam().findIfCollection().getType().getModel();	
				
				mapsToListModel.getLockTemplate().execute(()->{
					// only scenario to handle here is of "clear" of list model, all other scenarios should have been handled at elem-event level
//					if(mapsToListModel.size() != 0)
//						throw new InvalidStateException("Update state event for List Model can be processed only when state is initialized or reset. "
//								+ "Invalid state found for mappedParam: "+getPath()+" with mapsTo: "+mapsToListModel.getPath());
					
					// clear mapped
					clear();
				});
			}
		};

		getMapsTo().registerSubscriber(this);
	}
}
