/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import com.antheminc.oss.nimbus.core.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationResult;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.StateType;
import com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.core.entity.Findable;
import com.antheminc.oss.nimbus.platform.spec.model.dsl.binder.Holder;
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
	
	@JsonIgnore
	final private Model<?> parentModel;
	
//	@JsonIgnore M7
//M8	private Model<StateContextEntity> contextModel;
	
	@JsonIgnore
	private boolean active = true;
	
//	private boolean visible = true;
//	
//	private boolean enabled = true;
	
	private RemnantState<Boolean> visible = new RemnantState<>(true);
	private RemnantState<Boolean> enabled = new RemnantState<>(true);
	
	private List<ParamValue> values;
	
	private Message message;
	
	/* TODO: Weak reference was causing the values to be GC-ed even before the builders got to building 
	 * Allow referenced subscribers to get garbage collected in scenario when same core is referenced by multiple views. 
	 * Life span of core may be longer than cumulative views possibly leading to memory leak.
	 */
	//@JsonIgnore @Getter(AccessLevel.PRIVATE)
	//final private List<WeakReference<MappedParam<?, T>>> weakReferencedEventSubscribers = new ArrayList<>();
	
	@JsonIgnore 
	List<MappedParam<?, T>> eventSubscribers = new ArrayList<>(); 
	
	
	@JsonIgnore final private PropertyDescriptor propertyDescriptor;

	@JsonIgnore
	private T transientOldState;

	
	public static class LeafState<T> extends DefaultParamState<T> implements LeafParam<T> {
		private static final long serialVersionUID = 1L;
		
		public LeafState(Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
			super(parentModel, config, aspectHandlers);
		}
	}
	
	public DefaultParamState(Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
		super(config, aspectHandlers);

		if(!isRoot()) Objects.requireNonNull(parentModel, "Parent model must not be null with code: "+getConfig().getCode());
		this.parentModel = parentModel;
		
		PropertyDescriptor pd = constructPropertyDescriptor();
		this.propertyDescriptor = pd;
	}
	
	@Override
	protected void initSetupInternal() {
//		setPathArr(resolvePath());
//		
//		setBeanPathArr(resolveBeanPath());
	}
	
	@Override
	public String getPath() {
		String parentPath = Optional.ofNullable(getParentModel()).map(Model::getPath).orElse("");
		
		String p = new StringBuilder(parentPath)
				.append(Constants.SEPARATOR_URI.code)
				.append(getConfig().getCode())
				.toString();
		
		p = resolvePath(p);
		return p;
	}
	
	@Override
	public String getBeanPath() {
		String parentPath = Optional.ofNullable(getParentModel()).map(Model::getBeanPath).orElse("");
		
		String p = new StringBuilder(parentPath)
				.append(Constants.SEPARATOR_URI.code)
				.append(getConfig().getBeanName())
				.toString();
		
		p = StringUtils.replace(p, "//", "/");
		return p;
	}
	
//	protected String[] resolvePath() {
//		String[] parentPath = getParentModel().getPathArr();
//		return resolvePath(parentPath, getConfig().getCode());
//	}
//	
//	protected String[] resolveBeanPath() {
//		String[] parentPath = getParentModel().getBeanPathArr();
//		return resolvePath(parentPath, getConfig().getBeanName());
//	}
	
	
	
//	public static String[] resolvePath(String[] parentPath, String code) {
//		return ArrayUtils.addAll(parentPath, Constants.SEPARATOR_URI.code, code);
//		return new StringBuilder(parentPath)
//				.append(Constants.SEPARATOR_URI.code)
//				.append(code)
//				.toString();	
//	}

	@Override
	protected void initStateInternal() {
		if(isNested())
			findIfNested().initState();
		
		// hook up on state load events
		onStateLoadEvent(this);

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

	//@JsonIgnore 
	@Override
	public ParamConfig<T> getConfig() {
		return (ParamConfig<T>)super.getConfig();
	}
	
	protected PropertyDescriptor constructPropertyDescriptor() {
		if(getParentModel()==null) return null;
		
		ModelConfig<?> mConfig = getParentModel().getConfig();
		
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(mConfig.getReferredClass(), getConfig().getBeanName());
		return pd;
	}

	@Override
	public void fireRules() {
//		// self
//		Optional.ofNullable(getRulesRuntime())
//			.ifPresent(rt->rt.fireRules(this));
//		
//		// self: on state change
//		
		// nested 
		Optional.ofNullable(findIfNested())
			.ifPresent(Model::fireRules);
//		
//		// parent
//		Optional.ofNullable(getParentModel())
//			.filter(m->!m.isRoot())
//			.ifPresent(Model::fireRules);
	}
	
	/**
	 * resurrect entity pojo state from model/param state
	 * @return
	 */
	@Override
	final public T getLeafState() {
		if(!isMapped())
			return getState();
		
		if(!isNested())
			return getState();
		
		// create new entity instance
		T entity = getAspectHandlers().getParamStateGateway().instantiate(this.getConfig().getReferredClass());
		
		// assign param values to entity instance attributes
		if(findIfNested()==null)
			return entity;
		
		if(findIfNested().templateParams().isNullOrEmpty())
			return entity;
		
		if(isCollection()) {
			List<Object> colEntity = (List<Object>)entity;
			for(Param<?> pColElem : findIfNested().getParams()) {
				Object colElemState = pColElem.getLeafState();
				colEntity.add(colElemState);
			}
		} else { // nested
			for(Param<?> pNestedParam : findIfNested().getParams()) {
				Object nestedParamLeafState = pNestedParam.getLeafState();
				if(nestedParamLeafState!=null)
					getAspectHandlers().getParamStateGateway().setValue(pNestedParam.getPropertyDescriptor().getWriteMethod(), entity, nestedParamLeafState);
			}
		}
		
		return entity;
	}
	
	@JsonIgnore
	@Override
	final public T getState() {
//		if(getType().getConfig().getReferredClass()==StateContextEntity.class) {
//			return (T)createOrGetRuntimeEntity();
//		}
		return getAspectHandlers().getParamStateGateway()._get(this);
	}
	
	@Override
	public final Action setState(T state) {
		if(!isActive() && state!=null && isStateInitialized())
			throw new InvalidConfigException("Param's state cannot be changed when inactive. param: "+this.getPath());

		return changeStateTemplate((rt, h, lockId)->affectSetStateChange(state, rt, h, lockId));
	}
	
	protected final Action affectSetStateChange(T state, ExecutionRuntime execRt, Holder<Action> h, String localLockId) {
		state = preSetState(state);		
		boolean isLeaf = isLeafOrCollectionWithLeafElems();
		final T localPotentialOldState = isLeaf ? getState() : null;
		
		Action a = getAspectHandlers().getParamStateGateway()._set(this, state); 
		
		if(a!=null) {
			
			if(isLeaf)
				setTransientOldState(localPotentialOldState);
			
			// hook up on state change events
			onStateChangeEvent(resolveRuntime().getTxnContext(), this, a);
			
			// handle collection with leaf
			if(isCollectionElem() && getParentModel().getAssociatedParam().isLeafOrCollectionWithLeafElems()) {
				// publish only if current elem holds lock. 
				// TODO: unhandled scenario if lock is acquired by another param, which is not self or listParent, and add is invoked..such as via rule 
				if(localLockId!=null) {
					Param<?> parentList = getParentModel().getAssociatedParam();
					onStateChangeEvent(resolveRuntime().getTxnContext(), parentList, a);
				}
			}
			
			// propagate state event change to parent nested model's associated param
			Optional.ofNullable(getParentModel()).map(Model::getAssociatedParam).filter(Param::isNested).filter(p->!p.isCollection())
			.ifPresent(mp->{
				onStateChangeEvent(resolveRuntime().getTxnContext(), mp, a);
			});
			
			emitNotification(new Notification<>(this, ActionType._updateState, this));
			h.setState(a);
			
			if(execRt.isStarted())
				emitEvent(a, this);
		}
		
		postSetState(a, state);
		return a;
	}

	
	protected T preSetState(T state) {
		return state;
	}
	protected void postSetState(Action change, T state) {}
	
	@Override
	public void onStateLoadEvent() {
		onStateLoadEvent(this);
	}

	// TODO : move to runtime.eventDelegate
	protected static void onStateLoadEvent(Param<?> p) {
		EventHandlerConfig eventHandlerConfig = p.getConfig().getEventHandlerConfig();
		if(eventHandlerConfig!=null && eventHandlerConfig.getOnStateLoadAnnotations()!=null) {
			eventHandlerConfig.getOnStateLoadAnnotations().stream()
				.forEach(ac->{
					OnStateLoadHandler<Annotation> handler = eventHandlerConfig.getOnStateLoadHandler(ac);
					handler.handle(ac, p);
				});
		}
	}
	
	@Override
	public void onStateChangeEvent(ExecutionTxnContext txnCtx, Action a) {
		onStateChangeEvent(txnCtx, this, a);
	}
	
	// TODO : move to runtime.eventDelegate
	protected static void onStateChangeEvent(ExecutionTxnContext txnCtx, Param<?> p, Action a) {
		EventHandlerConfig eventHandlerConfig = p.getConfig().getEventHandlerConfig();
		if(eventHandlerConfig!=null && eventHandlerConfig.getOnStateChangeAnnotations()!=null) {
			eventHandlerConfig.getOnStateChangeAnnotations().stream()
				.forEach(ac->{
					OnStateChangeHandler<Annotation> handler = eventHandlerConfig.getOnStateChangeHandler(ac);
					handler.handle(ac, txnCtx, new ParamEvent(a, p));
				});
		}
	}
	
	@JsonIgnore @Override
	public ExecutionModel<?> getRootExecution() {
		if(getParentModel() != null) 
			return getParentModel().getRootExecution();
		
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
	public void registerConsumer(MappedParam<?, T> subscriber) {
		if(this instanceof Notification.Consumer)
			throw new InvalidOperationAttemptedException("Registering subscriber for Mapped entities are not supported. Found for: "+this.getPath()
						+" while trying to add subscriber: "+subscriber.getPath());
		
		getEventSubscribers().add(subscriber);
	}
	
	@Override
	public boolean deregisterConsumer(MappedParam<?, T> subscriber) {
		return getEventSubscribers().remove(subscriber);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	final public void emitNotification(Notification<T> event) {
		resolveRuntime().emitNotification((Notification<Object>)event);
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
        	
        	else if(event.getActionType()==ActionType._resetModel)
        		onEventResetModel(event);
        	
        	else if(event.getActionType()==ActionType._newElem) 
        		onEventNewElem(event);
        	
        	else if(event.getActionType()==ActionType._deleteElem)
        		onEventDeleteElem(event);    		
        	
        	else if(event.getActionType()==ActionType._evalProcess)
        		onEventEvalProcess(event);    	
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
			
			onStateChangeEvent(resolveRuntime().getTxnContext(), consumingParam, event.getActionType().getAction());
		}
		
	    protected void onEventNewModel(Notification<M> event) {
	    	if(consumingParam.isNested()) {
	    		consumingParam.findIfNested().instantiateOrGet();
	    	}
	    }
	    protected void onEventResetModel(Notification<M> event) {}
	    protected void onEventNewElem(Notification<M> event) {}
	    protected void onEventDeleteElem(Notification<M> event) {}
	    
	    protected void onEventEvalProcess(Notification<M> event) {}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
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
		// parent?
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_URI_PARENT.code))
			return getParentModel().getAssociatedParam();
		
		// domain root?
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_URI_ROOT_DOMAIN.code))
			return getRootDomain().getAssociatedParam();
		
		// execution root?
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_URI_ROOT_EXEC.code))
			return getRootExecution().getAssociatedParam();

		// context model?
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_CONFIG_ATTRIB.code))
			throw new InvalidConfigException("Use of '#' has been deprecated in favor of direct methods on Param, but found config at: "+getPath());

		// value is only ".m" then return mapsTo if this is a mapped param
		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_MAPSTO.code)) {
			if(isMapped())
				return findIfMapped().getMapsTo();
			else if(getRootDomain().isMapped())
				return getRootDomain().findIfMapped().getMapsTo().getAssociatedParam();
		}
		
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
		if(StringUtils.trimToNull(singlePathSegment)==null   // path = null or empty 
				|| Constants.SEPARATOR_URI.code.equals(StringUtils.trimToNull(singlePathSegment))  //  path = '/'
					)//|| StringUtils.contains(singlePathSegment, Constants.MARKER_COLLECTION_ELEM_INDEX.code)) // path = .../colModel/{index}/attrib
			return this;
		
//		// value is only ".m" then return mapsTo if this is a mapped param
//		if(StringUtils.equals(singlePathSegment, Constants.SEPARATOR_MAPSTO.code)) 
//			return isMapped() ? findIfMapped().getMapsTo() : null;
		
		return null;	
	}
	
	@Getter @Setter
	private static class RemnantState<T> {
		private T prevState;
		private T currState;
		
		public RemnantState(T initState) {
			this.prevState = initState;
			this.currState = initState;
		} 
		
		public void setState(T state) {
			this.prevState = this.currState;
			this.currState = state;
		}
	}
	
	
	@Override
	public boolean isVisible() {
		return visible.getCurrState();
	}
	
	@Override
	public boolean isEnabled() {
		return enabled.getCurrState();
	}
	
	public void setVisible(boolean visible) {
		if(this.visible.currState==visible)
			return;

		final boolean currActive = isActive();
		if(!currActive && visible)
			return;
		
		this.visible.setState(visible);
		emitParamContextEvent();
		
		// handle nested
		if(!isNested() || (isTransient() && !findIfTransient().isAssinged()))
			return;
		
		if (null == findIfNested().getParams()) {
			return;
		}
		
		findIfNested().getParams().stream()
			.forEach(p->{

				p.setVisible(visible);
				
			});
		
		findIfNested().getParams().stream()
		.forEach(p->{

			if(p.isStateInitialized())
				p.onStateLoadEvent();
			else
				p.onStateChangeEvent(p.getRootExecution().getExecutionRuntime().getTxnContext(), Action._update);
			
		});
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if(this.enabled.currState==enabled)
			return;
		
		final boolean currActive = isActive();
		if(!currActive && enabled)
			return;
		
		this.enabled.setState(enabled);
		emitParamContextEvent();
		
		// handle nested
		if(!isNested() || findIfNested().templateParams().isNullOrEmpty())
			return;
		
		if (null == findIfNested().getParams()) {
			return;
		}
		
		findIfNested().getParams().stream()
			.forEach(p->{

				p.setEnabled(enabled);
				
			});
	
		findIfNested().getParams().stream()
		.forEach(p->{

			if(p.isStateInitialized())
				p.onStateLoadEvent();
			else
				p.onStateChangeEvent(p.getRootExecution().getExecutionRuntime().getTxnContext(), Action._update);
			
		});
	
		
	}

	@Override
	public void setValues(List<ParamValue> values) {
		if(getValues()==values)
			return;
		
		this.values = values;
		emitParamContextEvent();
	}
	
	@Override
	public void setMessage(Message message) {
		if(getMessage()==null && message==null)
			return;
		
		if(getMessage()!=null && message!=null 
				&& getMessage().equals(message))
			return;
		
		this.message = message;
		emitParamContextEvent();
	}
	
	private void emitParamContextEvent() {
		resolveRuntime().emitEvent(new ParamEvent(Action._update, this));
	}
	
	@Override
	public boolean isFound(String path) {
		String resolvedPath = getResolvingMappedPath(path);
		return getConfig().isFound(resolvedPath);
	}
	
	@Override
	public boolean isActive() {
		if(!active)
			return active;
		
		Param<?> parentParam = Optional.ofNullable(getParentModel())
			.map(Model::getAssociatedParam)
			.orElse(null);
			
		if(parentParam==null)
			return active;
		
		if(!parentParam.isActive())
			return false;
		
		return active;
	}
	
	@Override
	public void activate() {
		toggleActivate(true);
	}
	
	@Override
	public void deactivate() {
		toggleActivate(false);
	}
	
	protected boolean toggleActivate(boolean to) {
		return changeStateTemplate((rt, h, lockId)->{
			boolean result = affectToggleActivate(to);
			if(result) {
				h.setState(Action._update);
				
				emitParamContextEvent();
			}
			
			return result;
		});
	}
	
	private boolean affectToggleActivate(boolean to) {
		// refer to field directly, instead of getter method
		if(active==to)
			return false;

		// toggle
		setActive(to);
		//setVisible(to); //this.visible.setState(to); //
		//setEnabled(to); //this.enabled.setState(to); //
		
		// notify mapped subscribers, if any
		//==emitNotification(new Notification<>(this, ActionType._active, this));
		
		// set state in current
		if(!to) {
			setStateInitialized(false);
			
			if(!isPrimitive())
				setState(null);
		} else {
			initState(); // ensure all rules are fired
		}
		
		setVisible(to); //this.visible.setState(to); //
		setEnabled(to); //this.enabled.setState(to); //
		
		// ripple to children, if applicable
		if(!isNested() || findIfNested().templateParams().isNullOrEmpty())
			return true;
		
		findIfNested().getParams().stream()
			.forEach(cp->{
				if(to)
					cp.initState();//cp.activate();
				else {
					cp.setStateInitialized(false);//cp.deactivate();
				}
				
				cp.setVisible(to); //this.visible.setState(to); //
				cp.setEnabled(to); //this.enabled.setState(to); //

			});
		
		return true;
	}
	
	private boolean isPrimitive() {
		return ClassUtils.isPrimitiveOrWrapper(getConfig().getReferredClass());
	}
	
}
