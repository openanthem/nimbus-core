/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.util.List;
import java.util.Optional;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends EntityConfig<T> {
	
	default String getAlias() {
		return null;
//		return (getDomain()!=null) 
//					? Optional.ofNullable(getDomain()).map(Domain::value).orElse(null)
//							: Optional.ofNullable(getModel()).map(Model::value).orElse(null);
	}

	public String getDomainLifecycle();
	
	public Repo getRepo();

	public List<? extends ParamConfig<?>> getParams();
	
	public ParamConfig<?> getIdParam();
	public ParamConfig<?> getVersionParam();
	
	@JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams();
	
	@JsonIgnore
	public RulesConfig getRulesConfig();

	@Override
	default MappedModelConfig<T, ?> findIfMapped() {
		return null;
	}
	
	default boolean isRoot() {
		return false;
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
