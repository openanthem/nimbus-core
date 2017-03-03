/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderContext;
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
	
	public MappedDefaultListParamState(ListParam<M> mapsTo, Model<?> parentModel, ParamConfig<List<T>> config, StateBuilderContext provider) {
		super(parentModel, config, provider);
		this.mapsTo = mapsTo;
		
		this.delegate = new InternalNotificationConsumer<List<M>>(this) {
			
			@Override
			protected void onEventNewElem(Notification<List<M>> event) {
				
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
			}
			
			@Override
			protected void onEventDeleteElem(Notification<List<M>> event) {
				logit.trace(()->"[onEventDeleteElem] received for mapsTo.ListParamElem: "+event.getEventParam().getPath());
				
				String elemId = event.getEventParam().findIfCollectionElem().getElemId();
				
				ListModel<T> mappedColModel = (ListModel<T>)getType().findIfNested().getModel().findIfListModel();
				Param<?> mappedParamElem = mappedColModel.templateParams().remove(elemId);
				
				logit.trace(()->"[onEventDeleteElem] removed mapped.ListParamElem: "+mappedParamElem.getPath());
			}
		};

		getMapsTo().registerSubscriber(this);
	}
}
