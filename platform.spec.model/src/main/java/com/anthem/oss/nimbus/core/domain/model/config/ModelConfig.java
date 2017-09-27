/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends EntityConfig<T> {
	
	public String getAlias();
	
	public String getDomainLifecycle();
	
	public Repo getRepo();

	public List<? extends ParamConfig<?>> getParams();
	
	public ParamConfig<?> getIdParam();
	public ParamConfig<?> getVersionParam();
	
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams();
	
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
