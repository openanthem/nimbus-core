/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.anthem.oss.nimbus.core.domain.Repo;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends Config<T> {
	
	public Repo getRepo();

	public List<? extends ParamConfig<?>> getParams();
	
	@JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams();

	@Override
	default MappedModelConfig<T, ?> findIfMapped() {
		return null;
	}
	
	
	public interface MappedModelConfig<T, M> extends ModelConfig<T>, MappedConfig<T, M> {
		
		@Override
		default MappedModelConfig<T, M> findIfMapped() {
			return this;
		}
		
		@Override
		public ModelConfig<M> getMapsTo();
	}
	
}
