/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

/**
 * @author Soham Chakravarti
 *
 */
public interface EntityConfig<T> {

	public String getId();
	
	public Class<T> getReferredClass();
	
	public <K> ParamConfig<K> findParamByPath(String path);
	public <K> ParamConfig<K> findParamByPath(String[] pathArr);

	
	default public boolean hasRules() {
		return getRulesConfig()!=null;
	}
	
	public RulesConfig getRulesConfig();
	
	default boolean isMapped() {
		return false;
	}
	
	default public MappedConfig<T, ?> findIfMapped() {
		return null;
	}

	public interface MappedConfig<T, M> extends EntityConfig<T> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default public MappedConfig<T, ?> findIfMapped() {
			return this;
		}
		
		public EntityConfig<M> getMapsTo();
	}
}
