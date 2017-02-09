/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.ListParam;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;


/**
 * @author Soham Chakravarti
 *
 */
public class DomainListParamState<T> extends DomainParamState<List<T>> implements ListParam<T> {
	private static final long serialVersionUID = 1L;
	
	public DomainListParamState(Model<?> parentModel, ParamConfig<List<T>> config, StateAndConfigSupportProvider provider) {
		super(parentModel, config, provider);
	}
	
	@Override
	public DomainStateType.NestedCollection<T> getType() {
		return super.getType().findIfCollection();
	}
	
	protected DomainState.ListModel<T> getNestedCollectionModel() {
		return getType().getModel();
	}
	
	@Override
	public T get(int i) {
		return getNestedCollectionModel().get(i);
	}
	
	@Override
	public ListElemParam<T> add() {
		return getNestedCollectionModel().add();
	}
	
	@Override
	public boolean add(T elem) {
		return getNestedCollectionModel().add(elem);
	}
	
	@Override
	public int size() {
		return getNestedCollectionModel().size();
	}

}
