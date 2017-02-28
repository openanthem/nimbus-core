/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListModel;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DefaultListModelState<T> extends DefaultModelState<List<T>> implements ListModel<T> {

	private static final long serialVersionUID = 1L;
	
	final private DefaultListElemParamState.Creator<T> elemCreator;
	
	public DefaultListModelState(ListParam<T> associatedParam, ModelConfig<List<T>> config, StateBuilderSupport provider, DefaultListElemParamState.Creator<T> elemCreator) {
		super(associatedParam, config, provider);
		this.elemCreator = elemCreator;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public T get(int i) {
		Param<T> p = (Param<T>)templateParams().getElem(i);
		return p.getState();
	}

	@Override
	public String toElemId(int i) {
		return String.valueOf(i);
	}
	@Override
	public int fromElemId(String elemId) {
		return new Integer(elemId);
	}

	@Override
	public List<T> instantiateAndSet() {
		List<T> newInstance =  super.instantiateAndSet();
		
		// reset collection elements
		if(!templateParams().isNullOrEmpty()) {
			int size = templateParams().size();
			for(int i=size-1; i>=0; i--) {
				
				Param<?> pColElem = templateParams().get().remove(i);
				
				// notify
				getAssociatedParam().notifySubscribers(new Notification<>(this.getAssociatedParam(), ActionType._deleteElem, pColElem));
			}
		}
		
		return newInstance;
	}
	
	public void remove(ListElemParam<T> pELem) {
		
	}
	
	@Override
	public ListElemParam<T> add() {
		return getLockTemplate().execute(()-> {
			List<T> list = instantiateOrGet();
			
			if(list.size()!=templateParams().size() && (
					isMapped() && getAssociatedParam().findIfMapped().requiresConversion()
					))  {
//				throw new InvalidStateException("List entity has size: "+list.size()+" whereas ListModel.params has size: "+templateParams().size()+". "
//						+ "Must be same but found different.");
			}
		
			String elemId = toElemId(templateParams().size());
			
			Param<T> pElem = getElemCreator().apply(this, elemId);
			ListElemParam<T> pColElem = pElem.findIfCollectionElem();
			templateParams().add(pColElem);
			
			// notify
			getAssociatedParam().notifySubscribers(new Notification<>(this.getAssociatedParam(), ActionType._newElem, pColElem));
			
			return pColElem;
		});
	}
	
	@Override
	public boolean add(T elem) {
		ListElemParam<T> pColElem = add();
		lockTemplate.execute(()->pColElem.setState(elem));
		return true;
	}
	
	
	@Override
	public int size() {
		return templateParams().size();
	}

}
