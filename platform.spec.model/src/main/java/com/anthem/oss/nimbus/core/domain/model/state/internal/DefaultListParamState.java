/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;


/**
 * @author Soham Chakravarti
 *
 */
public class DefaultListParamState<T> extends DefaultParamState<List<T>> implements ListParam<T> {
	private static final long serialVersionUID = 1L;
	
	public DefaultListParamState(Model<?> parentModel, ParamConfig<List<T>> config, EntityStateAspectHandlers provider) {
		super(parentModel, config, provider);
	}
	
	@Override
	public StateType.NestedCollection<T> getType() {
		return super.getType().findIfCollection();
	}
	
	protected EntityState.ListModel<T> getNestedCollectionModel() {
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
