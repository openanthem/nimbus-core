/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DefaultListModelState<T> extends DefaultModelState<List<T>> implements ListModel<T> {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore 
	final private DefaultListElemParamState.Creator<T> elemCreator;
	
	public static class StateContextListModelState<T> extends DefaultListModelState<T> {
		private static final long serialVersionUID = 1L;
		
		public StateContextListModelState(ListParam<T> associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
			super(associatedParam, config, provider, elemCreator);
		}
		
		@JsonIgnore @Override
		public ModelConfig<List<T>> getConfig() {
			return super.getConfig();
		}
		
		@JsonIgnore @Override
		public ParamConfig<T> getElemConfig() {
			return super.getElemConfig();
		}
	}
	
	public DefaultListModelState(ListParam<T> associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		super(associatedParam, config, provider);
		this.elemCreator = elemCreator;
	}
	
	public static <T> DefaultListModelState<T> instantiate(ListParam<T> associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		if(isStateContextConfig(associatedParam, config))
			return new StateContextListModelState<>(associatedParam, config, provider, elemCreator);
		
		return new DefaultListModelState<>(associatedParam, config, provider, elemCreator);
	}
	
	@Override
	protected void initStateInternal() {
		List<?> colEntityState = isMapped() ? findIfMapped().getMapsTo().getState() : getState();
		
		if(CollectionUtils.isEmpty(colEntityState))
			return;
		
		colEntityState.stream()
			.map(entityElem->add())
			.forEach(Param::initState);
	}

	@Override
	public List<T> instantiateAndSet() {
		return changeStateTemplate((execRt, h)->{
			clear();
			
			List<T> newInstance =  super.instantiateAndSet();
			return newInstance;			
		});
	}

	@Override
	public ListElemParam<T> createElement(String elemId) {
		return getElemCreator().apply(this, elemId).findIfCollectionElem();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListParam<T> getAssociatedParam() {
		return super.getAssociatedParam().findIfCollection();
	}
	
	@Override
	public T getState(int i) {
		return (T)getAssociatedParam().getState(i);
	}

	@Override
	public T getLeafState(int i) {
		return getAssociatedParam().getLeafState(i);
	}
	
	@Override
	public String toElemId(int i) {
		return getAssociatedParam().toElemId(i);
	}
	@Override
	public int fromElemId(String elemId) {
		return getAssociatedParam().fromElemId(elemId);
	}

	@Override
	public int size() {
		return getAssociatedParam().size();
	}

	@Override
	public ListElemParam<T> add() {
		return getAssociatedParam().add();
	}
	
	@Override
	public boolean add(T elem) {
		return getAssociatedParam().add(elem);
	}
	
	@Override
	public boolean add(ListElemParam<T> pColElem) {
		return getAssociatedParam().add(pColElem);
	}
	
	@Override
	public boolean remove(ListElemParam<T> pColElem) {
		return getAssociatedParam().remove(pColElem);
	}
	
	@Override
	public void clear() {
		getAssociatedParam().clear();
	}
	
	@Override
	public boolean contains(Param<?> other) {
		return getAssociatedParam().contains(other);
	}
}
