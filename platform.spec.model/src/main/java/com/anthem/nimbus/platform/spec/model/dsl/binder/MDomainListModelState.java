/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.List;
import java.util.Objects;

import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.MappedListModel;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MDomainListModelState<T, M> extends DomainListModelState<T> implements MappedListModel<T, M> {
	
	private static final long serialVersionUID = 1L;
	
	final private ListModel<M> mapsTo; 
	
	public MDomainListModelState(ListModel<M> mapsTo, ListParam<T> associatedParam, ModelConfig<List<T>> config, StateAndConfigSupportProvider provider, DomainListElemParamState.Creator<T> elemCreator) {
		super(associatedParam, config, provider, elemCreator);
		
		Objects.requireNonNull(mapsTo, "MapsTo model must not be null.");
		this.mapsTo = mapsTo;
	}
	
}
