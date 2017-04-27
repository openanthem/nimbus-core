/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;
import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedListModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
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
