/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;


import java.beans.PropertyDescriptor;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.EntityConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.anthem.oss.nimbus.core.spec.contract.event.EventListener;
import com.anthem.oss.nimbus.core.util.JustLogit;
import com.anthem.oss.nimbus.core.util.LockTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public abstract class AbstractEntityState<T> implements EntityState<T> {

	final private EntityConfig<T> config;
	
	@Setter(AccessLevel.PROTECTED) private String path;
	
	@Setter(AccessLevel.PROTECTED) private String beanPath;
	
	@JsonIgnore final private EntityStateAspectHandlers aspectHandlers;
	
	@JsonIgnore final protected LockTemplate lockTemplate = new LockTemplate();
	
	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());
	
	@JsonIgnore private RulesRuntime rulesRuntime;
	
	private boolean stateInitialized;
	
	public AbstractEntityState(EntityConfig<T> config, EntityStateAspectHandlers aspectHandlers) {
		Objects.requireNonNull(config, "Config must not be null while instantiating StateAndConfig.");
		Objects.requireNonNull(aspectHandlers, "Provider must not be null while instantiating StateAndConfig.");
		
		this.aspectHandlers = aspectHandlers;
		this.config = config;
	}
	
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
		if(!getRootExecution().getParamRuntimes().containsKey(getPath()))
			getRootExecution().getParamRuntimes().put(getPath(), new StateContextEntity());
		
		return getRootExecution().getParamRuntimes().get(getPath());
	}

	@FunctionalInterface
	public static interface ChangeStateCallback<R> {
		public R affectChange(ExecutionRuntime execRt, Holder<Action> h);
	}

	final protected <R> R changeStateTemplate(ChangeStateCallback<R> cb) {
		ExecutionRuntime execRt = resolveRuntime();
		String lockId = execRt.tryLock();
		final Holder<Action> h = new Holder<>();
		try {
			R resp = cb.affectChange(execRt, h);
			
			// fire rules if available at this param level
			fireRules();
			
			return resp;
		} finally {
			if(execRt.isLocked(lockId)) {
				// await completion of notification events
				execRt.awaitNotificationsCompletion();
				
				// fire rules at root level upon completion of all set actions
				getRootExecution().fireRules();
				
				// unlock
				boolean b = execRt.tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during setState of: "+getPath()+" with acquired lockId: "+lockId); 
			}
			
			if(h.getState()!=null && (this instanceof Notification.Producer)) {
				((Notification.Producer<?>)this).getEventSubscribers().forEach((subscriber) -> emitEvent(h.getState(), subscriber));
			}

		}
	}
	
	final protected void txnTemplate(Consumer<String> cb) {
		ExecutionRuntime execRt = resolveRuntime();
		String lockId = execRt.tryLock();
		try {
			cb.accept(lockId);
			
		} finally {
			if(execRt.isLocked(lockId)) {
			
				boolean b = execRt.tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during txn execution of: "+getPath()+" with acquired lockId: "+lockId);
			}
		}
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

	
	// TODO: SOHAM - need to refactor part of persistence changes
	public String getPath() {
		String p = path;

		p = StringUtils.replace(path, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		p = StringUtils.replace(p, "/f/", "/");
		return p;
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
