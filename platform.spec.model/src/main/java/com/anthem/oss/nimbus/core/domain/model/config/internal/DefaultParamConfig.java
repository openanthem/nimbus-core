/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"type", "validations", "values"})
public class DefaultParamConfig<P> extends AbstractEntityConfig<P> implements ParamConfig<P>, Serializable {

	private static final long serialVersionUID = 1L;

	final private String code;
	final private String beanName;

	private ParamType type;	

	private Desc desc = new Desc();
	
	private List<AnnotationConfig> validations;
	
	private List<ParamValue> values;
	
	private List<AnnotationConfig> uiNatures;

	private AnnotationConfig uiStyles;

	private ParamConfig<?> contextParam;
	
	@JsonIgnore 
	private transient Supplier<List<ParamValue>> valuesGetter;
	
	@JsonIgnore 
	private List<ParamConverter> converters;

	public static class StateContextConfig<P> extends DefaultParamConfig<P> {
		private static final long serialVersionUID = 1L;

		public StateContextConfig(String code, String beanName) {
			super(code, beanName);
		}
		
		@Override
		final public ParamConfig<?> getContextParam() {
			return null;
		}
		
		@Override
		final public void setContextParam(ParamConfig<?> runtimeConfig) {
			//do nothing
		}
	}
	
	protected DefaultParamConfig(String code) {
		this(code, code);
	}
	
	protected DefaultParamConfig(String code, String beanName) {
		this.code = code;
		this.beanName = beanName;
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code) {
		return instantiate(mConfig, code, code);
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code, String beanName) {
		if(mConfig.getReferredClass()==StateContextEntity.class)
			return new DefaultParamConfig.StateContextConfig<>(code, beanName);
		
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

		/* param is not leaf node */
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
	
	public List<ParamValue> getValues() {
		if(getValuesGetter() != null) {
			List<ParamValue> values = getValuesGetter().get();
			if(values != null) 
				this.values = values;
		}
		return values;
	}
	
}
