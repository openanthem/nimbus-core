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
	
	@SuppressWarnings("unchecked")
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, String elemId) {
		this(parentModel, config, provider, (ListElemParam<M>)findOrCreateMapsTo(parentModel, elemId));
	}
	
	public MappedDefaultListElemParamState(ListModel<E> parentModel, ParamConfig<E> config, EntityStateAspectHandlers provider, ListElemParam<M> mapsTo) {
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
