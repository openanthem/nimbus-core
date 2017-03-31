/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationResult;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderContext;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.entity.Findable;
import com.anthem.oss.nimbus.core.spec.contract.event.EventListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultParamState<T> extends AbstractEntityState<T> implements Param<T>, Findable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private StateType type;
	
	private ValidationResult validationResult;
	
	final private Model<?> parentModel;
	
	private StateContextEntity runtime;
	
	private Model<?> contextModel;
	
	/* TODO: Weak reference was causing the values to be GC-ed even before the builders got to building 
	 * Allow referenced subscribers to get garbage collected in scenario when same core is referenced by multiple views. 
	 * Life span of core may be longer than cumulative views possibly leading to memory leak.
	 */
	//@JsonIgnore @Getter(AccessLevel.PRIVATE)
	//final private List<WeakReference<MappedParam<?, T>>> weakReferencedEventSubscribers = new ArrayList<>();
	@JsonIgnore List<MappedParam<?, T>> eventSubscribers = new ArrayList<>(); 
	
	
	@JsonIgnore final private PropertyDescriptor propertyDescriptor;
	
	public DefaultParamState(Model<?> parentModel, ParamConfig<T> config, StateBuilderContext provider) {
		super(config, provider);

		if(!isRoot()) Objects.requireNonNull(parentModel, "Parent model must not be null with code: "+getConfig().getCode());
		this.parentModel = parentModel;
		
		PropertyDescriptor pd = constructPropertyDescriptor();
		this.propertyDescriptor = pd;
	}
	
	@Override
	protected void initInternal() {
		String rPath = resolvePath();
		setPath(rPath);
	}
	
	protected String resolvePath() {
		final String parentPath = StringUtils.trimToEmpty(getParentModel()==null ? "" : getParentModel().getPath());
		
		return new StringBuilder(parentPath)
					.append(Constants.SEPARATOR_URI.code)
					.append(getConfig().getCode())
					.toString();
	}

	
//	TODO: Refactor with Weak reference implementation
//	@JsonIgnore
//	@Override
//	public List<MappedParam<?, T>> getEventSubscribers() {
//		List<MappedParam<?, T>> eventSubscribers = getWeakReferencedEventSubscribers().stream()
//			.filter(e->e!=null)
//			.map(e->e.get())
//				.filter(mp->mp!=null)
//				.collect(Collectors.toList());
//
//		return eventSubscribers==null ? Collections.emptyList() : eventSubscribers;
//	}

	@Override
	public ParamConfig<T> getConfig() {
		return (ParamConfig<T>)super.getConfig();
	}
	
	protected PropertyDescriptor constructPropertyDescriptor() {
		if(getParentModel()==null) return null;
		
		ModelConfig<?> mConfig = getParentModel().getConfig();
		
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(mConfig.getReferredClass(), getConfig().getCode());
		return pd;
	}

	@Override
	public void fireRules() {
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.fireRules(this));
	}
	
	/**
	 * resurrect entity pojo state from model/param state
	 * @return
	 */
	@Override
	final public T getLeafState() {
		if(!isNested())
			return getState();
		
		// create new entity instance
		T entity = getProvider().getParamStateGateway().instantiate(this.getConfig().getReferredClass());
		
		// assign param values to entity instance attributes
		Optional.ofNullable(findIfNested())
			.map(nm->nm.getParams())
			.orElse(Collections.emptyList())
			.stream()
			.filter(p->!p.isNested() || p.isCollectionElem())	// iterate nested only if parent param is collection
			.forEach(p->
				Optional.ofNullable(p.getLeafState())
					.ifPresent(leafState-> {
						if(p.isCollectionElem()) 
							((List<Object>)entity).add(leafState); 
						else		
							getProvider().getParamStateGateway().setValue(p.getPropertyDescriptor().getWriteMethod(), entity, leafState);
						}
					)
			);

		return entity;
	}
	
	@JsonIgnore
	@Override
	final public T getState() {
		if(getType().getConfig().getReferredClass()==StateContextEntity.class) {
			return (T)createOrGetRuntimeEntity();
		}
		return getProvider().getParamStateGateway()._get(this);
	}
	
	@Override
	final public Action setState(T state) {
		ExecutionRuntime execRt = getRootExecution().getExecutionRuntime();
		String lockId = execRt.tryLock();
		final Holder<Action> h = new Holder<>();
		try {
			state = preSetState(state);			
			Action a = getProvider().getParamStateGateway()._set(this, state); 
			if(a!=null) {
				notifySubscribers(new Notification<>(this, ActionType._updateState, this));
				h.setState(a);
				emitEvent(a, this);
			}
			
			postSetState(a, state);
			
			// fire rules if available at this param level
			fireRules();
			
			return a;
		} finally {
			if(execRt.isLocked(lockId)) {
				// await completion of notification events
				execRt.awaitCompletion();
				
				if(h.getState() != null) {
					this.eventSubscribers.forEach((subscriber) -> emitEvent(h.getState(), subscriber));
				}
				
				// fire rules at root level upon completion of all set actions
				getRootExecution().fireRules();
				
				// unlock
				boolean b = execRt.tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during setState of: "+getPath()+" with acquired lockId: "+lockId); 
			}	
		}
	}
	
	protected T preSetState(T state) {
		return state;
	}
	protected void postSetState(Action change, T state) {}
	
	protected void emitEvent(Action a , Param p) {
		if(getProvider().getEventListener() == null) return;
		
		ModelEvent<Param<?>> e = new ModelEvent<Param<?>>(a, p.getPath(), p);
		EventListener publisher = getProvider().getEventListener();
		publisher.listen(e);
	}
	
	@JsonIgnore @Override
	public ExecutionModel<?> getRootExecution() {
		if(getParentModel() != null) return getParentModel().getRootExecution();
		
		/* if param is root, then it has to be of type nested */
		if(!getType().isNested())
			throw new InvalidConfigException("Found param as root which is not nested: "+ this);
		
		return findIfNested().findIfRoot();
	}

	@JsonIgnore @Override
	public Model<?> getRootDomain() {
		if(getParentModel()!=null && getParentModel().isRoot()) {
			return findIfNested();
		}
		
		return getParentModel().getRootDomain();
	}

	@Override
	public void registerSubscriber(MappedParam<?, T> subscriber) {
		getEventSubscribers().add(subscriber);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	final public void notifySubscribers(Notification<T> event) {
		getRootExecution().getExecutionRuntime().notifySubscribers((Notification<Object>)event);
	}
	
	
	@Getter
	class InternalNotificationConsumer<M> implements Notification.Consumer<M> {
		final private MappedParam<T, M> consumingParam;
		
		public InternalNotificationConsumer(MappedParam<T, M> consumingParam) {
			Objects.requireNonNull(consumingParam, "Consumer must not be null");
			this.consumingParam = consumingParam;
		}
		
		@Override
		public void handleNotification(Notification<M> event) {
			logit.trace(()->"[handleEventInternal] triggered event: "+event);

	    	if(event==null) return;
    		if(!shouldProcessNotification(event)) return;
        	
        	if(event.getActionType()==ActionType._updateState)
        		onEventUpdateState(event);
        	
        	else if(event.getActionType()==ActionType._newModel) 
        		onEventNewModel(event);
        	
        	else if(event.getActionType()==ActionType._deleteModel)
        		onEventDeleteModel(event);
        	
        	else if(event.getActionType()==ActionType._newElem) 
        		onEventNewElem(event);
        	
        	else if(event.getActionType()==ActionType._deleteElem)
        		onEventDeleteElem(event);    		
	    }
	    
		protected boolean shouldProcessNotification(Notification<M> event) {
			return true;
		}
		
		protected void onEventUpdateState(Notification<M> event) {
			if(consumingParam.isNested()) {
				consumingParam.findIfNested().instantiateOrGet();
			} else {
				Optional.ofNullable(consumingParam.getParentModel()).ifPresent(m->m.instantiateOrGet());
			}	
		}
		
	    protected void onEventNewModel(Notification<M> event) {
	    	if(consumingParam.isNested()) {
	    		consumingParam.findIfNested().instantiateOrGet();
	    	}
	    }
	    protected void onEventDeleteModel(Notification<M> event) {}
	    protected void onEventNewElem(Notification<M> event) {}
	    protected void onEventDeleteElem(Notification<M> event) {}
	}
	
	
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		@SuppressWarnings("unchecked")
		final Param<P> _this = (Param<P>)this;
		
		// return self if no path is provided
		if(ArrayUtils.isEmpty(pathArr))
			return _this;

		// find param with top most array element
		final String currTopParamPathSegment = pathArr[0];
		final Param<P> currTopParam = (Param<P>)findParamByPathInSelf(currTopParamPathSegment);
		
		if(currTopParam!=null)
			return currTopParam;
		
		// look in nested model, if applicable
		Param<?> currTopNestedParam = findParamByPathInModel(currTopParamPathSegment);
		
		// return null if not found
		if(currTopNestedParam==null)
			return null;
		
		// if path array size is 1, the return self found
		if(pathArr.length==1)
			return (Param<P>)currTopNestedParam;
		
		// nested param found: recurse for size-1 path segments with currTop removed
		return currTopNestedParam.findParamByPath(ArrayUtils.remove(pathArr, 0));
	}

	public Param<?> findParamByPathInModel(String singlePathSegment) {
		if(getType().findIfNested()==null)
			return null;

		// param is not leaf node  
		final Model<?> associatedNestedModel = getType().findIfNested().getModel();

		// find nested param: passed in path may contain "code" or "code.m"	
		Param<?> currNestedParam = associatedNestedModel.templateParams().find(singlePathSegment);
		if(currNestedParam==null)
			return null;
		
		// found self: return self if path is only "code"
		if(!containsMapsToPath(singlePathSegment)) 
			return currNestedParam;
		
		// found self AND path is "code.m": return self if mapped 
		return currNestedParam.isMapped() ? currNestedParam.findIfMapped().getMapsTo() : null;
	}
	
	public Param<?> findParamByPathInSelf(String singlePathSegment) {
		// blank or null returns self
		if(StringUtils.trimToNull(singlePathSegment)==null)
			return this;
		
		// value is only ".m" then return mapsTo if this is a mapped param
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_MAPSTO.code)) 
			return isMapped() ? findIfMapped().getMapsTo() : null;
		
		return null;	
	}
	
	@Override
	public boolean isFound(String path) {
		String resolvedPath = getResolvingMappedPath(path);
		return getConfig().isFound(resolvedPath);
	}
}
