/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends EntityConfig<T> {
	
	public Repo getRepo();

	public List<? extends ParamConfig<?>> getParams();
	
	@JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams();
	
	@JsonIgnore
	public RulesConfig getRulesConfig();

	@Override
	default MappedModelConfig<T, ?> findIfMapped() {
		return null;
	}
	
	
	public interface MappedModelConfig<T, M> extends ModelConfig<T>, MappedConfig<T, M> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default MappedModelConfig<T, M> findIfMapped() {
			return this;
		}
		
		@Override
		public ModelConfig<M> getMapsTo();
	}
	
}
