/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

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

	@JsonIgnore private final ListElemParam<M> mapsTo;
	
	@JsonIgnore final private Notification.Consumer<M> delegate;
	
	@SuppressWarnings("unchecked")
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, String elemId) {
		this(parentModel, config, provider, (ListElemParam<M>)findOrCreateMapsTo(parentModel, elemId));
	}
	
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, ListElemParam<M> mapsTo) {
		super(parentModel, config, provider, mapsTo.getElemId());
		this.mapsTo = mapsTo;		
		
		this.delegate = new InternalNotificationConsumer<>(this);
		
		getMapsTo().registerSubscriber(this);
	}
	
	public static <E> ListElemParam<?> findOrCreateMapsTo(ListModel<E> parentModel, String elemId) {
		MappedListModel<E, ?> mappedParentModel = parentModel.findIfMapped();
		
		//check if mapsToElem already exists
		ListModel<?> mapsToModel = mappedParentModel.getMapsTo();
		
		Param<?> maps2Param = mapsToModel.templateParams().find(elemId);
		final ListElemParam<?> mapsToElem;
		
		if(maps2Param!=null) 
			mapsToElem = maps2Param.findIfCollectionElem();
		else 
			mapsToElem = mapsToModel.add();
		
		return mapsToElem;
	}
}
