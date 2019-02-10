/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.internal;


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.util.ClassUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationResult;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.antheminc.oss.nimbus.domain.model.state.Notification.ActionType;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.domain.model.state.support.DefaultJsonParamSerializer;
import com.antheminc.oss.nimbus.entity.Findable;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@JsonSerialize(using=DefaultJsonParamSerializer.class)
@Getter @Setter
public class DefaultParamState<T> extends AbstractEntityState<T> implements Param<T>, Findable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private StateType type;
	
	private ValidationResult validationResult;
	
	@JsonIgnore
	final private Model<?> parentModel;
	
	@JsonIgnore
	private boolean active = true;
	
	@JsonIgnore
	private RemnantState<Boolean> visibleState = this.new RemnantState<>(true);
	
	@JsonIgnore
	private RemnantState<Boolean> enabledState = this.new RemnantState<>(true);
	
	@JsonIgnore
	@SuppressWarnings("unchecked")
	private RemnantState<Class<? extends ValidationGroup>[]> activeValidationGroupsState = new RemnantState<>(new Class[0]);
	
	private List<ParamValue> values;
	
	@JsonIgnore
	private RemnantState<Set<Message>> messageState = this.new RemnantState<Set<Message>>(null) {
		@Override
		public boolean hasChanged() {
			Set<Message> prev = CollectionUtils.isEmpty(getPrevState()) ? null : getPrevState();
			Set<Message> curr = CollectionUtils.isEmpty(getCurrState()) ? null : getCurrState();
			
			boolean isEquals = new EqualsBuilder().append(curr, prev).isEquals();
			return !isEquals;
		}
	};
	
	@JsonIgnore
	private RemnantState<Set<LabelState>> labelState = this.new RemnantState<Set<LabelState>>(null) {
		@Override
		public boolean hasChanged() {
			Set<LabelState> prev = CollectionUtils.isEmpty(getPrevState()) ? null : getPrevState();
			Set<LabelState> curr = CollectionUtils.isEmpty(getCurrState()) ? null : getCurrState();
			
			boolean isEquals = new EqualsBuilder().append(curr, prev).isEquals();
			return !isEquals;
		}
	};
	
	@JsonIgnore
	private RemnantState<StyleState> styleState = this.new RemnantState<>(null);
	
	@Override
	public boolean hasContextStateChanged() {
		return visibleState.hasChanged() || enabledState.hasChanged() || messageState.hasChanged() || 
				activeValidationGroupsState.hasChanged() || labelState.hasChanged() || 
				styleState.hasChanged();
	}
	
	
	/* TODO: Weak reference was causing the values to be GC-ed even before the builders got to building 
	 * Allow referenced subscribers to get garbage collected in scenario when same core is referenced by multiple views. 
	 * Life span of core may be longer than cumulative views possibly leading to memory leak.
	 */
	//@JsonIgnore @Getter(AccessLevel.PRIVATE)
	//final private List<WeakReference<MappedParam<?, T>>> weakReferencedEventSubscribers = new ArrayList<>();
	
	@JsonIgnore 
	List<MappedParam<?, T>> eventSubscribers = new ArrayList<>(); 
	
	
	@JsonIgnore 
	final ValueAccessor valueAccessor;

	public static class LeafState<T> extends DefaultParamState<T> implements LeafParam<T> {
		private static final long serialVersionUID = 1L;
		
		public LeafState(Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
			super(parentModel, config, aspectHandlers);
		}
		
		@JsonIgnore
		@Override
		public boolean isLeaf() {
			return true;
		}
		
		@Override
		public LeafState<T> findIfLeaf() {
			return this;
		}
	}
	
	public DefaultParamState(Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
		super(config, aspectHandlers);

		if(!isRoot()) Objects.requireNonNull(parentModel, "Parent model must not be null with code: "+getConfig().getCode());
		this.parentModel = parentModel;
		
		this.valueAccessor = constructValueAccessor();
	}
	
	
	protected ValueAccessor constructValueAccessor() {
		if(getParentModel()==null) 
			return null;
		
		ValueAccessor vaConfig = getConfig().getType().getValueAccessor();
		if(vaConfig!=null) 
			return vaConfig;
		
		ModelConfig<?> mConfig = getParentModel().getConfig();
		return JavaBeanHandlerUtils.constructValueAccessor(mConfig.getReferredClass(), getConfig().getBeanName());
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
	
	@Override
	protected void initStateInternal() {
		if(isNested()) {
			findIfNested().initState();
		}
		
		// hook up on state load events
		onStateLoadEvent(this);

	}
	
	public void onTypeAssign() {
		
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
		
		return getMappedState(this);
	}
	
	final static protected <T> T getMappedState(Param<T> p) {
		if(!p.isNested())
			return p.getState();
		
		// create new entity instance
		T entity = p.getAspectHandlers().getParamStateGateway().instantiate(p.getConfig().getReferredClass());
		
		// assign param values to entity instance attributes
		if(p.findIfNested()==null)
			return entity;
		
		if(p.findIfNested().templateParams().isNullOrEmpty())
			return entity;
		
		if(p.isCollection()) {
			List<Object> colEntity = (List<Object>)entity;
			for(Param<?> pColElem : p.findIfNested().getParams()) {
				Object colElemState = getMappedState(pColElem);//pColElem.getLeafState();
				colEntity.add(colElemState);
			}
		} else { // nested
			for(Param<?> pNestedParam : p.findIfNested().getParams()) {
				Object nestedParamLeafState = getMappedState(pNestedParam);//pNestedParam.getLeafState();
				if(nestedParamLeafState!=null)
					p.getAspectHandlers().getParamStateGateway().setValue(pNestedParam.getValueAccessor(), entity, nestedParamLeafState);
			}
		}
		
		return entity;
	}
	
	@JsonIgnore
	@Override
	final public T getState() {
		return getAspectHandlers().getParamStateGateway()._get(this);
	}
	
	public interface SetStateListener<T> {
		default T preSetState(T oldState, T state, String localLockId, ExecutionRuntime execRt) {
			return state;
		}
		
		default Action postSetState(Action change, T oldState, T state, String localLockId, ExecutionRuntime execRt) {
			return change;
		}
		
		@Getter @RequiredArgsConstructor
		public static class ListenerContext<T, X> {
			private final T state;
			private final X data;
		}
	}
	
	@JsonIgnore
	protected SetStateListener<T> getSetStateListener() {
		return null;
	}

	
	@Override
	//TODO: Temporary fix to disable active state check. JIRA NIM-9020 created to address this issue. 
	// A separate copy method needs to be introduced to resolve the issue
	public final Action setState(T state) {
		//if(!isActive() && state!=null && isStateInitialized())
			//throw new InvalidConfigException("Param's state cannot be changed when inactive. param: "+this.getPath());

		return changeStateTemplate((rt, h, lockId)->affectSetStateChange(state, rt, h, lockId, getSetStateListener()));
	}
	
	
	protected final Action setState(T state, SetStateListener<T> cb) {
		return changeStateTemplate((rt, h, lockId)->affectSetStateChange(state, rt, h, lockId, cb));
	}
	

	
	protected final <X> Action affectSetStateChange(T state, ExecutionRuntime execRt, Holder<Action> h, String localLockId, SetStateListener<T> cb) {
		boolean isLeaf = isLeafOrCollectionWithLeafElems();
		final T localPotentialOldState = isLeaf ? getState() : null;

		state = preSetState(localPotentialOldState, state, localLockId, execRt, cb);
		
		Action a = getAspectHandlers().getParamStateGateway()._set(this, state); 
		
		if(a!=null) {
			
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
		
		return postSetState(a, localPotentialOldState, state, localLockId, execRt, cb);
	}

	
	protected T preSetState(T oldState, T state, String localLockId, ExecutionRuntime execRt, SetStateListener<T> cb) {
		return cb==null ? state : cb.preSetState(oldState, state, localLockId, execRt);
	}
	protected Action postSetState(Action change, T oldState, T state, String localLockId, ExecutionRuntime execRt, SetStateListener<T> cb) {
		return cb==null ? change : cb.postSetState(change, oldState, state, localLockId, execRt);
	}
	
	
	protected void triggerEvents() {
		if(isStateInitialized())
			onStateLoadEvent();
		else
			onStateChangeEvent(getRootExecution().getExecutionRuntime().getTxnContext(), Action._update);
	}
	
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
					if(handler.shouldAllow(ac, p)) {
						handler.onStateLoad(ac, p);
					}
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
					if(handler.shouldAllow(ac, p)) {
						handler.onStateChange(ac, txnCtx, new ParamEvent(a, p));
					}
				});
		}
	}
	
	@JsonIgnore 
	@Override
	public ExecutionModel<?> getRootExecution() {
		if(getParentModel() != null) 
			return getParentModel().getRootExecution();
		
		/* if param is root, then it has to be of type nested */
		if(!getType().isNested())
			throw new InvalidConfigException("Found param as root which is not nested: "+ this);
		
		return findIfNested().findIfRoot();
	}

	@JsonIgnore 
	@Override
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
		
		return null;	
	}
	
	@Getter @Setter @ToString(of="currState")
	protected class RemnantState<S> {
		private S prevState;
		private S currState;
		
		public RemnantState(S initState) {
			this.prevState = initState;
			this.currState = initState;
		}
		
		public boolean isEquals(S state) {
			return new EqualsBuilder().append(this.currState, state).isEquals();
		}
		
		public boolean setState(S state) {
			return setStateConditional(state, ()->true);
		}

		public boolean setStateConditional(S state, Supplier<Boolean> condition) {
			// check equality
			if(isEquals(state)) {
				//this.prevState = this.currState;
				return false;
			}
			
			// check condition
			Boolean eval = condition.get();
			if(eval==null || !eval) {
				//this.prevState = this.currState;
				return false;
			}
			
			return changeStateTemplate((rt, h, lockId)->{
				this.prevState = this.currState;
				this.currState = state;
				
				emitParamContextEvent();
				return true;
			});
			
		}
		
		public boolean hasChanged() {
			boolean isEquals = new EqualsBuilder().append(this.currState, this.prevState).isEquals();
			return !isEquals;
		}
	}
	
	@Override
	public boolean isVisible() {
		return visibleState.getCurrState();
	}
	
	public void setVisible(boolean visible) {
		boolean changed = this.visibleState.setStateConditional(visible, ()->isActive() || !visible);
		if (!changed)
			return;
		
		// handle nested
		if(!isNested() /*|| (isTransient() && !findIfTransient().isAssinged())*/)
			return;
		
		if (null == findIfNested().getParams()) {
			return;
		}
		
		traverseChildren(p -> p.setVisible(visible));
		
		traverseChildren(p -> {
			
			if(!p.isStateInitialized())
				p.onStateLoadEvent();
			else
				p.onStateChangeEvent(p.getRootExecution().getExecutionRuntime().getTxnContext(), Action._update);
			
		});
	}
	
	@Override
	public boolean isEnabled() {
		return enabledState.getCurrState();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		boolean changed = this.enabledState.setStateConditional(enabled, ()->isActive() || !enabled);
		if (!changed)
			return;
		
		// handle nested
		if(!isNested() /*|| findIfNested().templateParams().isNullOrEmpty()*/)
			return;
		
		if (null == findIfNested().getParams()) {
			return;
		}
		
		traverseChildren(p -> p.setEnabled(enabled));
	
		traverseChildren(p -> {
			
			if(!p.isStateInitialized())
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
	public Set<LabelState> getLabels() {
		return Optional.ofNullable(this.labelState.getCurrState()).map(Collections::unmodifiableSet).orElse(null);
	}
	
	@Override
	public LabelState getDefaultLabel() {
		return getLabel(Locale.getDefault().toLanguageTag());
	}
	
	@Override
	public LabelState getLabel(String localeLanguageTag) {
		Set<LabelState> labelState = getLabels();
		if (null == labelState) {
			return null;
		}

		LabelState label = labelState.stream().filter(ls -> localeLanguageTag.equals(ls.getLocale())).reduce((a, b) -> {
			throw new IllegalStateException(
					"Found more than one element with " + localeLanguageTag + " on param " + this);
		}).orElse(null);

		return label;
	}
	
	@Override
	public void setLabels(Set<LabelState> labels) {
		Set<LabelState> labelstate = CollectionUtils.isEmpty(labels) ? null : new HashSet<>(labels);
		
		/*	check for multiple labels with same locale, 
			when setLabels in invoked manually instead of using @Label annotation */
		if(CollectionUtils.isNotEmpty(labelstate)) { 
			Set<LabelState> other = new HashSet<>(labelstate);	
			labelstate.stream().forEach(ls1 -> {
				other.stream().filter(ls2 -> (StringUtils.equalsIgnoreCase(ls1.getLocale(), ls2.getLocale()) 
						&& !StringUtils.equalsIgnoreCase(ls1.getText(), ls2.getText())))
				.findFirst()
				.ifPresent(label -> {
					throw new FrameworkRuntimeException("Label must have unique entries by locale,"
							+ " found multiple entries in Param path: "+this.getPath()
							+ " with repeating locale for LabelState: "+ label);
				});
			});
		}
		
		
		this.labelState.setState(labelstate);
	}
	
	@Override
	public Set<Message> getMessages() {
		return Optional.ofNullable(this.messageState.getCurrState()).map(Collections::unmodifiableSet).orElse(null);
	}

	@Override
	public void setMessages(Set<Message> msgs) {
		Set<Message> inMsgs = CollectionUtils.isEmpty(msgs) ? null : new HashSet<>(msgs);
		this.messageState.setState(inMsgs);
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
		
		setVisible(to); 
		setEnabled(to); 
		
		// ripple to children, if applicable
		if(!isNested() /*|| findIfNested().templateParams().isNullOrEmpty()*/)
			return true;
		
		if(findIfNested().getParams() == null && this.isCollection()) {
			return true;
		}
		
		traverseChildren(cp -> {
			
			if(to)
				cp.initState();//cp.activate();
			else {
				cp.setStateInitialized(false);//cp.deactivate();
			}
			
			cp.setVisible(to); 
			cp.setEnabled(to); 
	
		});
		
		return true;
	}
	
	private boolean isPrimitive() {
		return ClassUtils.isPrimitiveOrWrapper(getConfig().getReferredClass());
	}

	@Override
	public Class<? extends ValidationGroup>[] getActiveValidationGroups() {
		return this.activeValidationGroupsState.getCurrState();
	}

	@Override
	public void setActiveValidationGroups(Class<? extends ValidationGroup>[] activeValidationGroups) {
		this.activeValidationGroupsState.setState(activeValidationGroups);
	}

	@JsonIgnore
	@Override
	public boolean isLeaf() {
		return false;
	}

	@JsonIgnore
	@Override
	public boolean isLeafOrCollectionWithLeafElems() {
		return isLeaf() || (isCollection() && findIfCollection().isLeafElements());
	}

	@Override
	public LeafParam<T> findIfLeaf() {
		return null;
	}

	@Override
	public MappedParam<T, ?> findIfMapped() {
		return null;
	}

	@Override
	public boolean isCollection() {
		return false;
	}

	@Override
	public boolean isNested() {
		return getType().isNested();
	}

	@Override
	public Model<T> findIfNested() {
		return isNested() ? getType().<T>findIfNested().getModel() : null;
	}

	@Override
	public boolean isCollectionElem() {
		return false;
	}

	@Override
	public ListParam findIfCollection() {
		return null;
	}

	@Override
	public ListElemParam<T> findIfCollectionElem() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isLinked() {
		return false;
	}

	@Override
	public Param<?> findIfLinked() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public MappedTransientParam<T, ?> findIfTransient() {
		return null;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName()).append("(")
					.append("path=").append(getPath())
					.append(", mapped=").append(isMapped())
					.append(", refClass=").append(getConfig().getReferredClass().getSimpleName())
					.append(", state=").append(getState())
					.append(")")
					.toString();
	}

	@Override
	public StyleState getStyle() {
		return this.styleState.getCurrState();
	}

	@Override
	public void setStyle(StyleState styleState) {
		this.styleState.setState(styleState);
	}

	@Override
	public boolean isEmpty() {
		T leafState = this.getLeafState();
		if (leafState == null) {
			return true;
		}
		if (String.class == this.getType().getConfig().getReferredClass()) {
			return ((String) leafState).isEmpty();
		}
		return false;
	}
}