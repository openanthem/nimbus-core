/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Objects;

import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.MappedListElemParam;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MDomainListElemParamState<E, M> extends DomainListElemParamState<E> implements MappedListElemParam<E, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final ListElemParam<M> mapsTo;
	
	@JsonIgnore final private Notification.Consumer<M> delegate;
	
	public MDomainListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, StateAndConfigSupportProvider provider, String elemId) {
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
