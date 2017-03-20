/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.io.Serializable;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Mode;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.model.state.internal.RuntimeEntity;
import com.anthem.oss.nimbus.core.entity.Findable;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamConfig<P> extends EntityConfig<P>, Findable<String> {

	@lombok.Data
	public static class Desc implements Serializable {
		private static final long serialVersionUID = 1L;

		private String label;
		private String hint;
		private String help;
	}
	
	public String getCode();
	
	public ParamType getType();
	
	public boolean isLeaf();
	
	public Desc getDesc();
	
	public List<AnnotationConfig> getValidations();
	
	public AnnotationConfig getUiStyles();
	
	public List<ParamValue> getValues();
	
	public String getValuesUrl();
	
	public List<ParamConverter> getConverters();
	
	public ParamConfig<RuntimeEntity> getRuntimeConfig();
	
	default MapsTo.Mode getMappingMode() {
		return MapsTo.Mode.UnMapped;
	}
	
	@Override
	default MappedParamConfig<P, ?> findIfMapped() {
		return null;
	}
	
	public interface MappedParamConfig<P, M> extends ParamConfig<P>, MappedConfig<P, M> {
		@Override
		default MappedParamConfig<P, M> findIfMapped() {
			return this;
		}
		
		public Path getPath();

		@Override
		default Mode getMappingMode() {
			return MapsTo.getMode(getPath());
		}
		
		@Override
		public ParamConfig<M> getMapsTo();
		
		public ModelConfig<?> getMapsToEnclosingModel();
	}
}
