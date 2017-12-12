/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;


import java.beans.PropertyDescriptor;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.antheminc.oss.nimbus.core.entity.process.ProcessFlow;
import com.antheminc.oss.nimbus.core.spec.contract.event.EventListener;
import com.antheminc.oss.nimbus.core.util.JustLogit;
import com.antheminc.oss.nimbus.core.util.LockTemplate;
import com.antheminc.oss.nimbus.platform.spec.model.dsl.binder.Holder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public abstract class AbstractEntityState<T> implements EntityState<T> {

	final private EntityConfig<T> config;
	
//	private String path;
//	private String beanPath;
	
	//private String[] pathArr;
	//private String[] beanPathArr;
	
//	private String pathSegment;
//	private String beanPathSegment;
	
	@JsonIgnore final private EntityStateAspectHandlers aspectHandlers;
	
	@JsonIgnore final protected LockTemplate lockTemplate = new LockTemplate();
	
	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());
	
	@JsonIgnore private RulesRuntime rulesRuntime;
	
	@JsonIgnore private boolean stateInitialized;
	
	public AbstractEntityState(EntityConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
		Objects.requireNonNull(config, ()->"Config must not be null while instantiating StateAndConfig.");
		Objects.requireNonNull(aspectHandlers, ()->"Provider must not be null while instantiating StateAndConfig.");
		
		this.aspectHandlers = aspectHandlers;
		this.config = config;
	}
	
	// TODO: SOHAM - need to refactor part of persistence changes
	protected static String resolvePath(String p) {
		p = StringUtils.replace(p, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		//p = StringUtils.replace(p, "/f/", "/");
		
		p = StringUtils.removeEnd(p, "/c");
		p = StringUtils.removeEnd(p, "/v");
		//p = StringUtils.removeEnd(p, "/f");
		
		p = StringUtils.replace(p, "//", "/");
		return p;
	}
	
	/*
	@Override
	public String getPath() {
		return convertArrayToPath(pathArr);
//		String p = path;
//
//		p = StringUtils.replace(path, "/c/", "/");
//		p = StringUtils.replace(p, "/v/", "/");
//		p = StringUtils.replace(p, "/f/", "/");
//		return p;
	}

//	protected void setPath(String path) {
//		this.path = path;
//	}
	
	@Override
	public String getBeanPath() {
		return convertArrayToBeanPath(beanPathArr);
	}
//	public void setBeanPath(String beanPath) {
//		this.beanPath = beanPath;
//	}
	
	private final static String[] pathIgnores = new String[]{"c", "v", "f"};
	private String convertArrayToPath(String[] arr) {
		if(ArrayUtils.isEmpty(arr))
			return "";
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<arr.length; i++) {
			if(!ArrayUtils.contains(pathIgnores, arr[i]))
				sb.append(arr[i]);
		}
		
		return sb.toString();
	}
	
	private String convertArrayToBeanPath(String[] arr) {
		if(ArrayUtils.isEmpty(arr))
			return "";
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<arr.length; i++) {
			sb.append(arr[i]);
		}
		
		return sb.toString();
	}
	*/
	
	@Override
	final public void initSetup() {
		initSetupInternal();
		
		// start rules runtime/session
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.start());
	}
	
	protected void initSetupInternal() {} 
	
	@Override
	final public void initState() {
		if(isStateInitialized()) 
			return;
		
		ExecutionRuntime execRt = getRootExecution().getExecutionRuntime();
		String lockId = execRt.tryLock();
		try {
			initStateInternal();
			fireRules(); //TODO review with soham
			setStateInitialized(true); // From soham
		} finally {
			if(execRt.isLocked(lockId)) {
				execRt.awaitNotificationsCompletion();
				
				boolean b = execRt.tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during initState of: "+getPath()+" with acquired lockId: "+lockId);
			}
		}
	}
	
	protected void initStateInternal() {}
	
	protected Object createOrGetRuntimeEntity() {
//		if(!getRootExecution().getParamRuntimes().containsKey(getPath()))
//			getRootExecution().getParamRuntimes().put(getPath(), new StateContextEntity());
		
		return getRootExecution().getParamRuntimes().get(getPath());
	}

	@FunctionalInterface
	public static interface ChangeStateCallback<R> {
		public R affectChange(ExecutionRuntime execRt, Holder<Action> h, String localLockId);
	}

	final protected <R> R changeStateTemplate(ChangeStateCallback<R> cb) {
		ExecutionRuntime execRt = resolveRuntime();
		String lockId = execRt.tryLock();
		final Holder<Action> h = new Holder<>();
		try {
			R resp = cb.affectChange(execRt, h, lockId);
			
			// fire rules if available at this param level
			//fireRules();
			
			return resp;
		} finally {
			if(execRt.isLocked(lockId)) {
				logit.trace(()->"Executing within changeStateTemplate->finally block with lockId: "+lockId+" on param: "+this);
				
				// fire rules at root level upon completion of all set actions
				if(h.getState()!=null) 
					getRootExecution().fireRules();
				
				// notify subscribers to evaluate their process & rules
				Param<Object> domainRootParam = (Param<Object>)getRootDomain().getAssociatedParam();
				resolveRuntime().emitNotification(new Notification<Object>(domainRootParam, ActionType._evalProcess, domainRootParam));

				// await completion of notification events
				execRt.awaitNotificationsCompletion();
				
				// evaluate BPM
				evaluateProcessFlow();
				
				// unlock
				boolean b = execRt.tryUnlock(lockId);
				if(!b) {
					logit.warn(()->"Unable to gracefully unlock on param: "+this+" with lockId: "+lockId+" in thread: "+Thread.currentThread());
					//==throw new FrameworkRuntimeException("Failed to release lock acquired during setState of: "+getPath()+" with acquired lockId: "+lockId);
				}
			}
			
			if(h.getState()!=null && (this instanceof Notification.Producer)) {
				((Notification.Producer<?>)this).getEventSubscribers().forEach((subscriber) -> emitEvent(h.getState(), subscriber));
			}

		}
	}
	
	protected void evaluateProcessFlow() {
		String processExecId = Optional.ofNullable(getRootExecution().getState())
								.map(m->(ExecutionEntity<?, ?>)m)
								.map(ExecutionEntity::getFlow)
								.map(ProcessFlow::getProcessExecutionId)
								.orElse(null);
		if(processExecId!=null)
			getAspectHandlers().getBpmEvaluator().apply(getRootDomain().getAssociatedParam(), processExecId);
	}
	
	protected ExecutionRuntime resolveRuntime() {
		if(getRootExecution().getAssociatedParam().isLinked()) {
			return getRootExecution().getAssociatedParam().findIfLinked().getRootExecution().getExecutionRuntime();
		}
		
		return getRootExecution().getExecutionRuntime();
	}
	
	protected void emitEvent(Action a , Param p) {
		if(getAspectHandlers().getEventListener() == null) return;
		
		resolveRuntime().emitEvent(new ParamEvent(a, p));
		
		ModelEvent<Param<?>> e = new ModelEvent<Param<?>>(a, p.getPath(), p);
		EventListener listener = getAspectHandlers().getEventListener();
		listener.listen(e);
	}

	
	public static String getDomainRootAlias(EntityState<?> p) {
		Model<?> mapsToRootDomain = p.getRootDomain();
		return getDomainRootAlias(mapsToRootDomain.getConfig());
	}
	
	public static String getDomainRootAlias(ModelConfig<?> m) {
		Class<?> mapsToRootClass = m.getReferredClass();
		Domain domainAlias = AnnotationUtils.findAnnotation(mapsToRootClass, Domain.class);
		return domainAlias==null ? null : domainAlias.value();
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("[").append(getClass().getSimpleName()).append("]")
				.append(" path=").append(getPath())
				.append(" refClass=").append(getConfig().getReferredClass())
				.toString();
	}
	
	public boolean containsMapsToPath(String path) {
		return StringUtils.endsWith(path, Constants.SEPARATOR_MAPSTO.code);
	}
	
	public String getResolvingMappedPath(String path) {
		return containsMapsToPath(path)  ? StringUtils.stripEnd(path, Constants.SEPARATOR_MAPSTO.code) : path;
	}

	@Override
	public <P> Param<P> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}

	@Override
	public <S> Model<S> findModelByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findModelByPath(splits);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S> Model<S> findModelByPath(String[] pathArr) {
		Param<?> p = findParamByPath(pathArr);
		
		if(p == null && ArrayUtils.isEmpty(pathArr)) { // path = "/" and model is root (no parent)
			if(this instanceof Model) {
				return (Model<S>)this;
			}
			else {
				throw new InvalidConfigException("StateAndConfig of type "+this.getClass().getSimpleName()
						+" must have parent. Found StateAndConfig (Param?) with path: "+ getPath());
			}
		}

 		if(p==null) return null;
		
		Model<?> m = p.getType().findIfNested().getModel();
		return (m == null) ? null : (Model<S>)m;
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T, P> P read(PropertyDescriptor pd, Supplier<T> mGet) {
		try {
			T target = mGet.get();
			return (target == null) ? null : (P)pd.getReadMethod().invoke(target);
		}
		catch (Exception ex) {
			throw new FrameworkRuntimeException(ex);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// stop rules runtime/session
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.shutdown());
		
		super.finalize();
	}
}
