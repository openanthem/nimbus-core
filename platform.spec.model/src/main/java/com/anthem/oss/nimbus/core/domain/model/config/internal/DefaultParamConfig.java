/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"code", "beanName", "type"})
public class DefaultParamConfig<P> extends AbstractEntityConfig<P> implements ParamConfig<P>, Serializable {

	private static final long serialVersionUID = 1L;

	final private String code;
	final private String beanName;

	private ParamType type;	

	private Desc desc = new Desc();
	
	private List<AnnotationConfig> validations;
	
	private List<AnnotationConfig> uiNatures;
	
	@JsonIgnore
	private List<AnnotationConfig> rules;
	
	
	@JsonIgnore
	private List<Execution.Config> executionConfigs;

//	@JsonIgnore M7
//M8	private ParamConfig<StateContextEntity> contextParam;
	
	@JsonIgnore 
	private List<ParamConverter> converters;
	
	@JsonIgnore
	private Values values;
	
	@JsonIgnore @Setter 
	private List<AssociatedEntity> associatedEntities;


//	public static class StateContextConfig<P> extends DefaultParamConfig<P> {
//		private static final long serialVersionUID = 1L;
//
//		public StateContextConfig(String code, String beanName) {
//			super(code, beanName);
//		}
//		
//		@Override
//		final public ParamConfig<StateContextEntity> getContextParam() {
//			return null;
//		}
//		
//		@Override
//		final public void setContextParam(ParamConfig<StateContextEntity> runtimeConfig) {
//			//do nothing
//		}
//	}
	
	protected DefaultParamConfig(String code) {
		this(code, code);
	}
	
	protected DefaultParamConfig(String code, String beanName) {
		Objects.requireNonNull(code, ()->"code in param config must not be null");
		Objects.requireNonNull(beanName, ()->"beanName in param config must not be null");
		
		this.code = code.intern();
		this.beanName = beanName.intern();
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code) {
		return instantiate(mConfig, code, code);
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code, String beanName) {
//		if(mConfig.getReferredClass()==StateContextEntity.class)
//			return new DefaultParamConfig.StateContextConfig<>(code, beanName);
		
		return new DefaultParamConfig<>(code, beanName);
	} 
	

	@SuppressWarnings("unchecked")
	@Override
	public Class<P> getReferredClass() {
		return (Class<P>)getType().getReferredClass();
	}
	
	@Override
	public boolean isFound(String by) {
		return StringUtils.equals(getCode(), by);
	}
	
	@Override
	public boolean isLeaf() {
		return !getType().isNested();
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr))
			return null;
		
		/* param is not leaf node: is nested */
		ParamType.Nested<?> mp = getType().findIfNested();
		if(mp != null) {
			return mp.getModel().findParamByPath(pathArr);
		}
		
		/* if param is a leaf node and requested path has more children, then return null */
		if(pathArr.length > 1) return null;
		
		/* param is leaf node */
		ParamConfig<K> p = isFound(pathArr[0]) ? (ParamConfig<K>) this : null;
		return p;
	}
	
}
