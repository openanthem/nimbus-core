/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;


import java.beans.PropertyDescriptor;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.EntityConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.RulesRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.State;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderContext;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
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
	
	private String rootDomainUri;
	
	@JsonIgnore final private StateBuilderContext provider;
	
	@JsonIgnore final protected LockTemplate lockTemplate = new LockTemplate();
	
	@JsonIgnore final protected JustLogit logit = new JustLogit(getClass());
	
	@JsonIgnore private RulesRuntime rulesRuntime;
	
	public AbstractEntityState(EntityConfig<T> config, StateBuilderContext provider) {
		Objects.requireNonNull(config, "Config must not be null while instantiating StateAndConfig.");
		Objects.requireNonNull(provider, "Provider must not be null while instantiating StateAndConfig.");
		
		this.provider = provider;
		this.config = config;
	}
	
	@Override
	final public void init() {
		initInternal();
		
		// start rules runtime/session
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.start());
	}
	
	protected void initInternal() {} 
	
	protected StateContextEntity createOrGetRuntimeEntity() {
		if(!getRootExecution().getParamRuntimes().containsKey(getPath()))
			getRootExecution().getParamRuntimes().put(getPath(), new StateContextEntity());
		
		return getRootExecution().getParamRuntimes().get(getPath());
	}
	
	public String getPath() {
		String p = StringUtils.replace(path, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		p = StringUtils.replace(p, "/f/", "/");
		return p;
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
		StateType.Nested<S> pTypeNested = p.getType().findIfNested();
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
