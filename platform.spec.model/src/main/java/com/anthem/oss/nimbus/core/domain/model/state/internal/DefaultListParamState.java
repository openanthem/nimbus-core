/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
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
		clear(true);
	}
	
	protected void clear(boolean propagateToMapsTo) {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();

		rLockTemplate.execute(()->{
			if(getNestedCollectionModel().templateParams().isNullOrEmpty()) 
				return;
			
			changeStateTemplate((execRt, h) -> {
				// change state
				boolean result = affectClearChange(propagateToMapsTo);
				
				// notify
				emitNotification(new Notification<>(this, ActionType._resetModel, this));
				
				// emit event
				if(getRootExecution().getExecutionRuntime().isStarted())//if(execRt.isStarted())
					emitEvent(Action._replace, this);
				
				return result;
			});
		});
	}
	
	private boolean affectClearChange(boolean propagateToMapsTo) {
		// reset collection elements
		int size = getNestedCollectionModel().templateParams().size();
		boolean result = true;
		for(int i=size-1; i>=0; i--) {
			
			@SuppressWarnings("unchecked")
			ListElemParam<T> pColElem = (ListElemParam<T>)getNestedCollectionModel().getParams().get(i).findIfCollectionElem();
			boolean r = affectRemoveChange(pColElem, propagateToMapsTo);
			
			// don't change if result was marked false
			result = result ? r : result;
		}
	
		return result;
	}
	
	@Override
	public boolean remove(final ListElemParam<T> pElem) {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();
		
		return rLockTemplate.execute(()->{
			
			return changeStateTemplate((rt, h) -> {
				return affectRemoveChange(pElem, getRootExecution().getExecutionRuntime(), true);
			});
		});
	}
	
	private boolean affectRemoveChange(final ListElemParam<T> pElem, ExecutionRuntime execRt, boolean propagateToMapsTo) {
		// remove from entity state and collection
		boolean isRemoved = affectRemoveChange(pElem, propagateToMapsTo);
		
		if(isRemoved) {
			// notify state
			emitNotification(new Notification<>(this, ActionType._deleteElem, pElem));
			
			// emit event
			if(execRt.isStarted())
				emitEvent(Action._delete, pElem);
			
			return true;
		}

		return false;
	}

	 
	private boolean affectRemoveChange(ListElemParam<T> pElem, boolean propagateToMapsTo) {
		// if mapped, process remove on mapsTo first
		if(isMapped()) {
			
			if(propagateToMapsTo) {
				boolean mapsToRemoved = affectRemoveChangeIfMapped(pElem);
				return mapsToRemoved;
			} else {
				boolean mappedRemoved = affectRemoveIfMappedOrUnMapped(pElem);
				return mappedRemoved;
			}
		}

		// handle mapped or unmapped scenarios
		return affectRemoveIfMappedOrUnMapped(pElem);
	}
	
	
	@SuppressWarnings("unchecked")
	private <M> boolean affectRemoveChangeIfMapped(ListElemParam<T> pElem) {
		// elem must be mapped as well
		if(!pElem.isMapped())
			throw new InvalidStateException("MappedList cannot have Un-Mapped Collection Elements. "
					+ "Found MappedList: "+findIfMapped().getMapsTo()+" for elem: "+pElem);
		
		MappedListElemParam<T, ?> mappedElem = pElem.findIfMapped();
		Param<?> mapsToParam = mappedElem.getMapsTo();
		
		ListElemParam<M> mapsToElem = (ListElemParam<M>)findMapsToColElemByColElemNestedPath(mapsToParam, mappedElem.getConfig());
		
		ListParam<M> mapsToCol = (ListParam<M>)findIfMapped().getMapsTo().findIfCollection();
		
		return mapsToCol.remove(mapsToElem);
	}
	
	protected ListElemParam<?> findMapsToColElemByColElemNestedPath(Param<?> mapsToParam, ParamConfig<?> mappedElemConfig) {
		MapsTo.Path mapsToPath = mappedElemConfig.findIfMapped().getPath();
		
		// determine if @MapsTo.Path has colElem entry for nested colElem's param
		if(StringUtils.trimToNull(mapsToPath.colElemPath()) != null) {
			String mapsToColElemNestedPath = mapsToParam.getPath();
			String mapsToColElemPath = StringUtils.removeEnd(mapsToColElemNestedPath, mappedElemConfig.findIfMapped().getPath().colElemPath());
			
			ListElemParam<?> mapsToElem = mapsToParam.getRootExecution().findParamByPath(mapsToColElemPath).findIfCollectionElem();
			return mapsToElem;
			
		} 

		// otherwise, treat mapping is for collection
		return mapsToParam.findIfCollectionElem();	
	}
	
	private boolean affectRemoveIfMappedOrUnMapped(ListElemParam<T> pElem) {
		// remove from collection entity state
		List<T> currList = getState();//instantiateOrGet();
		T elemToRemove = pElem.getState();
		boolean isRemoved = currList.remove(elemToRemove);
		
		// remove from collection model state
		String elemId = pElem.getElemId();
		Param<?> pRemoved = pElem.getParentModel().templateParams().remove(elemId);
		
		// handle scenario that elem may already have been removed (e.g: mapsTo.remove would trigger deleteElem notification which wont find the element in mapped
		if(isRemoved && pRemoved!=null) {
			if(pRemoved!=pElem)
				throw new InvalidStateException("Colection Elem Param removed must of same instance but found different. "
						+ " Removed param path: "+pRemoved.getPath()
						+ " Intended remove param path: "+pElem.getPath());
			
			return true;
		}
	
		logit.warn(()->"Attempt to remove elem from collection did not succeed. ListParam: "+this.getPath()+" -> attempted elemId to remove: "+elemId);
		return false;
	}
	
	@Override
	public ListElemParam<T> add() {
		final LockTemplate rLockTemplate = isMapped() ? findIfMapped().getMapsTo().getLockTemplate() : getLockTemplate();
		
		return rLockTemplate.execute(()->{
			return changeStateTemplate((rt, h)->affectAddChange(rt));
		});
	} 
	
	private ListElemParam<T> affectAddChange(ExecutionRuntime execRt) {
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
		
		// place-holder in entity state
		if(pColElem.isMapped()) {
			int currMaxElemIndx = getNestedCollectionModel().templateParams().isNullOrEmpty() ?
								-1 : getNestedCollectionModel().templateParams().size()-1;//getMaxElemIndex();
			int currEntityListSize = list.size();
			
			if(currEntityListSize == currMaxElemIndx) 
				list.add(null);
			else if(currEntityListSize > currMaxElemIndx) {
				logit.trace(()->"ListParam: "+this+" is being setup with ParamElem being created for existing List entity already having colElems. "
						+ "Currently building ListElemParam: "+pElem);
			}
			else //if(pColElem.findIfMapped().requiresConversion())
				throw new InvalidStateException("EntityList size :"+currEntityListSize+" must be greater or equal than elemParam being added, but found currMaxElemIndx: "+currMaxElemIndx+" for param: "+pColElem);
		}
		
		// notify
		emitNotification(new Notification<>(this, ActionType._newElem, pColElem));
		
		if(execRt.isStarted())
			emitEvent(Action._new, this);
		
		return pColElem;
	}
	
	@Override
	public boolean add(T elem) {
		ListElemParam<T> pColElem = add();
		pColElem.setState(elem);		//lockTemplate.execute(()->pColElem.setState(elem));
		return true;
	}
	
	@Override
	public ListElemParam<T> createElement() {
		String elemId = toElemId(getNextElemIndex());
		
		ListElemParam<T> pColElem = getNestedCollectionModel().createElement(elemId);
		return pColElem;
	}
	
	@Override
	public boolean add(ListElemParam<T> pColElem) {
		List<T> list = getNestedCollectionModel().instantiateOrGet();
		
		// add
		getNestedCollectionModel().templateParams().add(pColElem);
		
		// notify
		emitNotification(new Notification<>(this, ActionType._newElem, pColElem));
		
		ExecutionRuntime execRt = resolveRuntime();
		if(execRt.isStarted())
			emitEvent(Action._new, this);
		
		return true;
	}

	@Override
	public boolean contains(Param<?> other) {
		return getNestedCollectionModel().templateParams().contains(other);
	}
}
