/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.util.LockTemplate;


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
	public int size() {
		return getNestedCollectionModel().templateParams().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getState(int i) {
		Param<T> p = (Param<T>)getNestedCollectionModel().templateParams().getElem(i);
		return p.getState();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getLeafState(int i) {
		Param<T> p = (Param<T>)getNestedCollectionModel().templateParams().getElem(i);
		return p.getLeafState();
	}
	
	@Override
	public String toElemId(int i) {
		return String.valueOf(i);
	}
	@Override
	public int fromElemId(String elemId) {
		return new Integer(elemId);
	}
	
	public int getNextElemIndex() {
		return getMaxElemIndex() + 1;
	}
	
	public int getMaxElemIndex() {
		if(getNestedCollectionModel().templateParams().isNullOrEmpty())
			return -1;
		
		ListElemParam<?> pMaxElemIndex = getNestedCollectionModel().getParams().stream()
			.reduce((currMax, currElem)-> {
				int currMaxIndex = fromElemId(currMax.findIfCollectionElem().getElemId());
				int currElemIndex = fromElemId(currElem.findIfCollectionElem().getElemId());
				
				return (currElemIndex>currMaxIndex) ? currElem : currMax;
			})
			.map(Param::findIfCollectionElem)
			.get();
		
		int maxIndex = fromElemId(pMaxElemIndex.getElemId());
		return maxIndex;
	}

	@Override
	public void clear() {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();

		rLockTemplate.execute(()->{
			if(getNestedCollectionModel().templateParams().isNullOrEmpty()) 
				return;
			
			//changeStateTemplate((execRt, h) -> {
				// change state
				boolean result = affectClearChange();
				
				// notify
				notifySubscribers(new Notification<>(this, ActionType._resetModel, this));
				
				// emit event
				if(getRootExecution().getExecutionRuntime().isStarted())//if(execRt.isStarted())
					emitEvent(Action._replace, this);
				
				return;// result;
			//});
		});
	}
	
	private boolean affectClearChange() {
		// reset collection elements
		int size = getNestedCollectionModel().templateParams().size();
		boolean result = true;
		for(int i=size-1; i>=0; i--) {
			
			@SuppressWarnings("unchecked")
			ListElemParam<T> pColElem = (ListElemParam<T>)getNestedCollectionModel().getParams().get(i).findIfCollectionElem();
			boolean r = affectRemoveChange(pColElem);
			
			// don't change if result was marked false
			result = result ? r : result;
		}
	
		return result;
	}
	
	@Override
	public boolean remove(final ListElemParam<T> pElem) {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();
		
		return rLockTemplate.execute(()->{
			
			//return changeStateTemplate((rt, h) -> {
				return affectRemoveChange(pElem, getRootExecution().getExecutionRuntime());
			//});
		});
	}
	
	private boolean affectRemoveChange(final ListElemParam<T> pElem, ExecutionRuntime execRt) {
		// remove from entity state and collection
		boolean isRemoved = affectRemoveChange(pElem);
		
		if(isRemoved) {
			// notify state
			notifySubscribers(new Notification<>(this, ActionType._deleteElem, pElem));
			
			// emit event
			if(execRt.isStarted())
				emitEvent(Action._delete, pElem);
			
			return true;
		}

		return false;
	}
	
	private boolean affectRemoveChange(ListElemParam<T> pElem) {
		// remove from collection entity state
		List<T> currList = getState();//instantiateOrGet();
		T elemToRemove = pElem.getState();
		boolean isRemoved = currList.remove(elemToRemove);
		
		// remove from collection model state
		String elemId = pElem.getElemId();
		Param<?> pRemoved = pElem.getParentModel().templateParams().remove(elemId);
		
		if(isRemoved && pRemoved!=null) {
			if(pRemoved!=pElem)
				throw new InvalidStateException("Colection Elem Param removed must of same instance but found different. "
						+ " Removed param path: "+pRemoved.getPath()
						+ " Intended Delete param path: "+pElem.getPath());
			
			return true;
		}
	
		logit.warn(()->"Attempt to remove elem from collection did not succeed. ListParam: "+this+" -> attempted elemId to remove: "+elemId);
		return false;
	}
	
	@Override
	public ListElemParam<T> add() {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();
		
		return rLockTemplate.execute(()->{
			//return changeStateTemplate((rt, h)->affectAddChange());
			return affectAddChange();
		});
	} 
	
	private ListElemParam<T> affectAddChange() {
		List<T> list = getNestedCollectionModel().instantiateOrGet();
		
		if(list.size()!=getNestedCollectionModel().templateParams().size() /*&& (
				isMapped() && getAssociatedParam().findIfMapped().requiresConversion()
				)*/)  {
			logit.error(()->
			/*throw new InvalidStateException(*/" 1  List entity has size: "+list.size()+" whereas ListModel.params has size: "+getNestedCollectionModel().templateParams().size()+". "
					+ "Must be same but found different.");
		}
	
		String elemId = toElemId(getNextElemIndex());
		
		Param<T> pElem = getNestedCollectionModel().createElement(elemId);
		ListElemParam<T> pColElem = pElem.findIfCollectionElem();
		getNestedCollectionModel().templateParams().add(pColElem);
		
		// notify
		notifySubscribers(new Notification<>(this, ActionType._newElem, pColElem));
		
		return pColElem;
	}
	
	@Override
	public boolean add(T elem) {
		ListElemParam<T> pColElem = add();
		pColElem.setState(elem);		//lockTemplate.execute(()->pColElem.setState(elem));
		return true;
	}
	
}
