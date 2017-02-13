/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListElemParam;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListModel;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.StateType.NestedCollection;


/**
 * @author Soham Chakravarti
 *
 */
public class DefaultListParamState<T> extends DefaultParamState<List<T>> implements ListParam<T> {
	private static final long serialVersionUID = 1L;
	
	public DefaultListParamState(Model<?> parentModel, ParamConfig<List<T>> config, StateBuilderSupport provider) {
		super(parentModel, config, provider);
	}
	
	@Override
	public StateType.NestedCollection<T> getType() {
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
