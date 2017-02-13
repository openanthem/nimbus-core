/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.MappedListElemParam;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
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
	
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, StateBuilderSupport provider, String elemId) {
		super(parentModel, config, provider, elemId);
		
		@SuppressWarnings("unchecked")
		ListElemParam<M> mapsTo = (ListElemParam<M>)findMapsTo(parentModel, elemId);
		Objects.requireNonNull(mapsTo, "MapsTo param must not be null.");
		this.mapsTo = mapsTo;		
		
		this.delegate = new InternalNotificationConsumer<>(this);
		
		getMapsTo().registerSubscriber(this);
	}
	
	public static <E> ListElemParam<?> findMapsTo(ListModel<E> parentModel, String elemId) {
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
