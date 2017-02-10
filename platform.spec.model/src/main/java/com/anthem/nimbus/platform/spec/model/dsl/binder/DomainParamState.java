/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.nimbus.platform.spec.model.Findable;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.command.ValidationResult;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Param;
import com.anthem.nimbus.platform.spec.model.dsl.binder.Notification.ActionType;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DomainParamState<T> extends AbstractDomainState<T> implements Param<T>, Findable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private DomainStateType type;
	
	private ValidationResult validationResult;
	
	final private Model<?> parentModel;
	
	/***
	 * Allow referenced subscribers to get garbage collected in scenario when same core is referenced by multiple views. 
	 * Longevity of core may be longer than cumulative views possibly leading to memory leak.
	 */
	@JsonIgnore @Getter(AccessLevel.PRIVATE)
	final private WeakReference<List<MappedParam<?, T>>> eventSubscribersRef = new WeakReference<List<MappedParam<?, T>>>(new ArrayList<>());
	
	
	@JsonIgnore final private PropertyDescriptor propertyDescriptor;
	
	public DomainParamState(Model<?> parentModel, ParamConfig<T> config, StateAndConfigSupportProvider provider) {
		super(config, provider);

		if(!isRoot()) Objects.requireNonNull(parentModel, "Parent model must not be null with code: "+getConfig().getCode());
		this.parentModel = parentModel;
		
		PropertyDescriptor pd = constructPropertyDescriptor();
		this.propertyDescriptor = pd;
	}
	
	@Override
	public List<MappedParam<?, T>> getEventSubscribers() {
		return eventSubscribersRef.get();
	}

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
	final public T getState() {
		return getProvider().getParamStateGateway()._get(this);
	}
	
	@Override
	final public Action setState(T state) {
		ExecutionRuntime execRt = getRootModel().getExecutionRuntime();
		String lockId = execRt.tryLock();
		try {
			state = preSetState(state);
			
			Action a = getProvider().getParamStateGateway()._set(this, state);
			if(a!=null)
				notifySubscribers(new Notification<>(this, ActionType._updateState, this));
			
			postSetState(a, state);
			return a;
		} finally {
			if(execRt.isLocked(lockId)) {
				// await completion of notification events
				execRt.awaitCompletion();
				
				// unlock
				boolean b = execRt.tryUnlock(lockId);
				if(!b)
					throw new PlatformRuntimeException("Failed to release lock acquired during setState of: "+getPath()+" with acquired lockId: "+lockId); 
			}	
		}
	}
	
	protected T preSetState(T state) {
		return state;
	}
	protected void postSetState(Action change, T state) {}
	
    
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
	
	
	@JsonIgnore @Override
	public RootModel<?> getRootModel() {
		if(getParentModel() != null) return getParentModel().getRootModel();
		
		/* if param is root, then it has to be of type nested */
		if(!getType().isNested())
			throw new InvalidConfigException("Found param as root which is not nested: "+ this);
		
		return getType().findIfNested().getModel().findIfRoot();
	}


	@Override
	public void registerSubscriber(MappedParam<?, T> subscriber) {
		getEventSubscribers().add(subscriber);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	final public void notifySubscribers(Notification<T> event) {
		getRootModel().getExecutionRuntime().notifySubscribers((Notification<Object>)event);
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
				consumingParam.getType().findIfNested().getModel().instantiateOrGet();
			} else {
				Optional.ofNullable(consumingParam.getParentModel()).ifPresent(m->m.instantiateOrGet());
			}	
		}
		
	    protected void onEventNewModel(Notification<M> event) {
	    	if(consumingParam.isNested()) {
	    		consumingParam.getType().findIfNested().getModel().instantiateOrGet();
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
	
//	public boolean isImplicitlyMapped() {
//		if(getConfig().getMapsToMode()!=MapsTo.Mode.UnMapped) return true;
//		
//		/* still considered mapped, if parent model is mapped and current param's nested model is mapped with same class */
//		ModelConfig<?> mConfig = getParentModel().getConfig();
//		
//		if(getParentModel().isMapped() && getConfig().getType().isNested()) {	//check if parent model is mapped AND current param is nested
//			
//			ModelConfig<?> mpNmConfig = getConfig().getType().findIfNested().getModel();
//			
//			if(mpNmConfig.isMapped()) {	//check if param.nestedModel is mapped AND is same parent's mapsTo model
//				
//				Class<?> mappedParamNmMapsToClass = mpNmConfig.getMapsTo().value();	//param.nestedModel mapsTo
//				Class<?> mMapsToClass = mConfig.getMapsTo().value();	//parent's mapsTo
//				
//				return mappedParamNmMapsToClass == mMapsToClass;
//			}
//		}
//			
//		return false;
//	}	
	
//	@Override
//	public void update(Observable o, Object arg) {
//		// TODO Auto-generated method stub
//		
//	}

	
	
//	@Override
//	public void setState(T state) {
//		ClientUser cu = this.getProvider().getClientUser();
//		
//		// delegate to model.setState if of type nested model
////		if(getType().isNested()) {
////			getType().findIfNested().getModel().setState(state);
////			return;
////		}
//		
//		Action a = setStateInternal(state);
//		
//		
//		boolean hasAccess = true;
//		if(a != null){
//			ClientUserAccessBehavior clientUserAccessBehavior = cu.newBehaviorInstance(ClientUserAccessBehavior.class);			
//			hasAccess = clientUserAccessBehavior.canUserPerformAction(getPath(),a.name());
//		}
//		
//		if(hasAccess){		
//			setStateAndNotifyObservers(state);
//			emitEvent(this);
//		}else{
//			log.debug("User does not have access to set state on ["+getPath()+"] for action : ["+a.name()+"]");
//			throw new UserNotAuthorizedException("User not authorized to set state on ["+getPath()+"] for action : ["+a.name()+"] " +this);
//		}
//			
//	}
	
	protected void emitEvent(DomainState<?> p) {
		if(getProvider().getEventPublisher() == null) return;
		if(p instanceof DomainParamState<?>) {
			DomainParamState<?> param = (DomainParamState<?>)p;
			ModelEvent<DomainParamState<?>> e = new ModelEvent<DomainParamState<?>>(Action._replace, param.getPath(), param);
			getProvider().getEventPublisher().publish(e);
		}
		else if(p instanceof DomainModelState<?>){
			DomainModelState<?> param = (DomainModelState<?>)p;
			ModelEvent<DomainModelState<?>> e = new ModelEvent<DomainModelState<?>>(Action._replace, param.getPath(), param);
			getProvider().getEventPublisher().publish(e);
		}
		//ModelEvent<ParamStateAndConfig<?>> e = new ModelEvent<ParamStateAndConfig<?>>(Action._replace, p.getPath(), p);
		
	}
	
	/**
	 * 
	 */
	@Override
	public void validateAndSetState(T state) {
		validate(state);
		
		if(CollectionUtils.isNotEmpty(this.validationResult.getErrors())) {
			emitEvent(this);
	    	throw new ValidationException(this.validationResult);
	    } 
		else {
	    	setState(state);
	    }
	}
	
	/**
	 * 
	 * @param state
	 */
	private void validate(T state) {
		this.validationResult = new ValidationResult();
	    //TODO change the hard coded bean class and property with the respective getter methods once available
		if(isMapped()) { 
			//for view mapped param
			Class<?> mappedParamClass = getConfig().getReferredClass();
			String mappedParamCode = getConfig().getCode();
			
			//for core mapsTo param
			Class<?> mapsToParamClass = findIfMapped().getConfig().getReferredClass();
			String mapsToParamCode = findIfMapped().getConfig().getCode();
			
			this.validationResult.addValidationErrors(getProvider().getValidatorProvider().getValidator().validateValue(mappedParamClass, mappedParamCode, state));
		    this.validationResult.addValidationErrors(getProvider().getValidatorProvider().getValidator().validateValue(mapsToParamClass, mapsToParamCode, state)); 
			
		} 
		else {
			Class<?> paramClass = getConfig().getReferredClass();
			String paramCode = getConfig().getCode();
			
			this.validationResult.addValidationErrors(getProvider().getValidatorProvider().getValidator().validateValue(paramClass, paramCode, state)); 
		} 
	}
	
}
