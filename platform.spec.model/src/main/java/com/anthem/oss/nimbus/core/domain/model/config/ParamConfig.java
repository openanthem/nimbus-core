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

		public boolean isLinked();
		
		default public MappedParamConfigLinked<P, M> findIfLinked() {
			return null;
		}
		
		default public MappedParamConfigDelinked<P, M> findIfDelinked() {
			return null;
		}
		
		@Override
		default Mode getMappingMode() {
			return MapsTo.getMode(getPath());
		}
	}
	
	public interface MappedParamConfigLinked<P, M> extends MappedParamConfig<P, M> {

		@Override
		default MappedParamConfigLinked<P, M> findIfMapped() {
			return this;
		}
		
		@Override
		default boolean isLinked() {
			return true;
		}
		
		@Override
		default MappedParamConfigLinked<P, M> findIfLinked() {
			return this;
		}
		
		@Override
		public ParamConfig<M> getMapsTo();
	}
	
	public interface MappedParamConfigDelinked<P, M> extends MappedParamConfig<P, M> {

		@Override
		default MappedParamConfigDelinked<P, M> findIfMapped() {
			return this;
		}
		
		@Override
		default boolean isLinked() {
			return false;
		}
		
		@Override
		default MappedParamConfigDelinked<P, M> findIfDelinked() {
			return this;
		}
		
		@Override
		public ModelConfig<M> getMapsTo();
	}
}
