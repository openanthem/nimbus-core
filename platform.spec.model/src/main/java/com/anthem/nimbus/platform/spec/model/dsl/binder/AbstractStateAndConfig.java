/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.config.Config;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"path", "config"})
public abstract class AbstractStateAndConfig<T, C extends Config<T>> extends AbstractState<T>
		implements StateAndConfig<T, C>, Serializable {
	

	private static final long serialVersionUID = 1L;

	private final C config;
	
	@JsonIgnore private transient final ValidatorProvider validatorProvider;
	
	@Setter private String path;
	
	@Setter private String rootDomainUri;
	
	public AbstractStateAndConfig(AbstractStateAndConfig<?, ?> parent, C config, StateAndConfigSupportProvider provider) {
		super(parent, provider);
		this.config = config;
		this.validatorProvider = provider.getValidatorProvider();
	}
	
	
	/**
	 * 
	 */
	@Override
	public <S> State<S> findStateByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findStateByPath(splits);
	}

	/**
	 * 
	 */
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
		StateAndConfig.Model<S, ? extends ModelConfig<S>> mp = p.getType().findIfNested();
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
	
	/**
	 * 
	 */
	@Override
	public <P> Param<P> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}
	
	/**
	 * 
	 */
	@Override
	public <S, M extends ModelConfig<S>> StateAndConfig.Model<S, M> findModelByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findModelByPath(splits);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S, M extends ModelConfig<S>> StateAndConfig.Model<S, M> findModelByPath(String[] pathArr) {
		Param<?> p = findParamByPath(pathArr);
		
		if(p == null && ArrayUtils.isEmpty(pathArr)) { // path = "/" and model is root (no parent)
			if(this instanceof StateAndConfig.Model) {
				return (StateAndConfig.Model<S, M>)this;
			}
			else {
				throw new InvalidConfigException("StateAndConfig of type "+this.getClass().getSimpleName()
						+" must have parent. Found StateAndConfig (Param?) with path: "+ getPath());
			}
		}
		
		StateAndConfig.Model<?, ?> m = p.getType().findIfNested();
		return (m == null) ? null : (StateAndConfig.Model<S, M>)m;
	}
	
	
	/**
	 * 
	 * @param pd
	 * @param mGet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, P> P read(PropertyDescriptor pd, Supplier<T> mGet) {
		try {
			T target = mGet.get();
			return (target == null) ? null : (P)pd.getReadMethod().invoke(target);
		}
		catch (Exception ex) {
			throw new PlatformRuntimeException(ex);
		}
	}
	
}
