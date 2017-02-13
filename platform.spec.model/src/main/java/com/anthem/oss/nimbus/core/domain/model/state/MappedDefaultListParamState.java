/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.oss.nimbus.core.domain.model.ParamConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultListParamState<T, M> extends DefaultListParamState<T> implements DomainState.MappedListParam<T, M>, NotificationConsumerDelegate<List<M>> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final ListParam<M> mapsTo;
	
	@JsonIgnore private final Notification.Consumer<List<M>> delegate;
	
	public MappedDefaultListParamState(ListParam<M> mapsTo, Model<?> parentModel, ParamConfig<List<T>> config, StateAndConfigSupportProvider provider) {
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
		};

		getMapsTo().registerSubscriber(this);
	}
}
