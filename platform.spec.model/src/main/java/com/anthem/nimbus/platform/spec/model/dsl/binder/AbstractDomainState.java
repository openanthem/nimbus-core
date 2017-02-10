/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;


import java.beans.PropertyDescriptor;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.config.Config;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
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
public abstract class AbstractDomainState<T> implements DomainState<T> {

	final private Config<T> config;
	
	@Setter(AccessLevel.PROTECTED) private String path;
	
	private String rootDomainUri;
	
	@JsonIgnore final private StateAndConfigSupportProvider provider;
	
	@JsonIgnore final private Lock lock = new ReentrantLock();
	
	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());
	
	
	@FunctionalInterface
	public interface LockCallback<T> {
		public T execute();
	}
	
	
	@FunctionalInterface
	public interface LockCallbackNoReturn {
		public void execute();
	}
	
	public AbstractDomainState(Config<T> config, StateAndConfigSupportProvider provider) {
		Objects.requireNonNull(config, "Config must not be null while instantiating StateAndConfig.");
		Objects.requireNonNull(provider, "Provider must not be null while instantiating StateAndConfig.");
		
		this.provider = provider;
		this.config = config;
	}
	
	@Override
	final public void init() {
		initInternal();
	}
	
	protected void initInternal() {} 
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("[").append(getClass().getSimpleName()).append("]")
				.append(" path=").append(getPath())
				.append(" refClass=").append(getConfig().getReferredClass())
				.toString();
	}

	
	final public <L> L lock(LockCallback<L> cb) {
		lock.lock();
		try{
			return cb.execute();
		} finally {
			lock.unlock();
		}
	}
	
	final public void lock(LockCallbackNoReturn cb) {
		lock.lock();
		try{
			cb.execute();
		} finally {
			lock.unlock();
		}
	}
	
	public boolean containsMapsToPath(String path) {
		return StringUtils.endsWith(path, Constants.SEPARATOR_MAPSTO.code);
	}
	
	public String getResolvingMappedPath(String path) {
		return containsMapsToPath(path)  ? StringUtils.stripEnd(path, Constants.SEPARATOR_MAPSTO.code) : path;
	}

	
	@Override
	public <S> State<S> findStateByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findStateByPath(splits);
	}

	@Override
	public <S> State<S> findStateByPath(String[] pathArr) {
		String lastElem = pathArr[pathArr.length - 1];
		
		/* check if the last path item has # entry */
		if(!StringUtils.contains(lastElem, Constants.SEPARATOR_CONFIG_ATTRIB.code)) {
			return findParamByPath(pathArr);
		}
		
		/* find param excluding # entry */
		final String lastElemArr[] = StringUtils.split(lastElem, Constants.SEPARATOR_CONFIG_ATTRIB.code);
		pathArr[pathArr.length - 1] = lastElemArr[0];
		
		Param<S> p = findParamByPath(pathArr);
		
		/* lookup state on param's config */
		DomainStateType.Nested<S> pTypeNested = p.getType().findIfNested();
		Model<S> mp = pTypeNested.getModel();
		Object pathRefConfig = (mp != null) ? mp.getConfig() : p.getConfig();
		
		/* get {#configAttribName}State */
		String propStateNm = lastElemArr[1] + Constants.SUFFIX_PROPERTY_STATE.code;
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(pathRefConfig.getClass(), propStateNm);
		
		if(pd == null && mp != null) {
			pd = BeanUtils.getPropertyDescriptor(p.getConfig().getClass(), propStateNm);
		}
		
		if(pd == null) {
			throw new InvalidConfigException("Property: "+ propStateNm + " not found on param: "+p);
		}
		
		State<S> state = read(pd, ()->pathRefConfig);
		return state;
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
			throw new PlatformRuntimeException(ex);
		}
	}
	
}
