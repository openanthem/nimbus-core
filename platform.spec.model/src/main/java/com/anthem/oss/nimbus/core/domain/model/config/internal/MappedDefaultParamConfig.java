/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.DetachedState;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.LoadState;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Mode;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Nature;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo", "path"})
public class MappedDefaultParamConfig<P, M> extends DefaultParamConfig<P> implements MappedParamConfig<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ModelConfig<?> mapsToEnclosingModel;
	
	@JsonIgnore	final private ParamConfig<M> mapsTo;

	@JsonIgnore final private Path path;
	
	public static class NoConversion<P, M> extends MappedDefaultParamConfig<P, M> {
		private static final long serialVersionUID = 1L;
		
		public NoConversion(ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo) {
			super(mapsTo.getCode(), mapsTo.getBeanName(), mapsToEnclosingModel, mapsTo, createNewImplicitMapping("", true));
			
			init(mapsTo);
		}
		
		private void init(ParamConfig<M> mapsTo) {
			setContextParam(mapsTo.getContextParam());
			setConverters(mapsTo.getConverters());
			setDesc(mapsTo.getDesc());
			setExecutionConfigs(mapsTo.getExecutionConfigs());
			setRules(mapsTo.getRules());
			setType(mapsTo.getType());
			setUiNatures(mapsTo.getUiNatures());
			setUiStyles(mapsTo.getUiStyles());
			setValidations(mapsTo.getValidations());
			setValues(mapsTo.getValues());
		}
		
		@Override
		public String getConfigId() {
			return super.getConfigId();
		}
	}
	
	public MappedDefaultParamConfig(String code, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo, Path path) {
		this(code, code, mapsToEnclosingModel, mapsTo, path);
	}
	
	public MappedDefaultParamConfig(String code, String beanName, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo, Path path) {
		super(code, beanName);
		this.mapsToEnclosingModel = mapsToEnclosingModel;
		this.mapsTo = mapsTo;
		this.path = path;
	}
	
	@JsonIgnore
	@Override
	public boolean isDetachedWithAutoLoad() {
		
		if(getMappingMode() == Mode.MappedDetached
				&& getPath().detachedState() != null
				&& getPath().detachedState().loadState() == LoadState.AUTO
				&& StringUtils.isNotBlank(getPath().value())) {
			
			return true;
		}
		return false;
	}
	
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked) {
		return createNewImplicitMapping("", true, new DetachedState() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MapsTo.DetachedState.class;
			}
			
			@Override
			public boolean manageState() {
				return false;
			}
			
			@Override
			public LoadState loadState() {
				return LoadState.PROVIDED;
			}
			
			@Override
			public Cache cacheState() {
				return Cache.rep_none;
			}
		});
	}
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked, DetachedState detachedState) {
		return createNewImplicitMapping(mappedPath, linked, "", detachedState);
	}
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked, String colElemPath, DetachedState detachedState) {
		return new MapsTo.Path() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MapsTo.Path.class;
			}
			
			@Override
			public String value() {
				return mappedPath;
			}
			
			@Override
			public boolean linked() {
				return linked;
			}
			
			@Override
			public String colElemPath() {
				return colElemPath;
			}
			
			@Override
			public DetachedState detachedState() {
				return detachedState;
			}
			
			@Override
			public Nature nature() {
				return Nature.Default;
			}
			
			@Override
			public String toString() {
				return new StringBuilder().append(MapsTo.Path.class)
							.append(" value: ").append(value())
							.append(" linked: ").append(linked())
							.toString();
			}
		};
	}
}
