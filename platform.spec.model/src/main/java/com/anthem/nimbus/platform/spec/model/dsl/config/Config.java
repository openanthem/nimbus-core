/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

/**
 * @author Soham Chakravarti
 *
 */
public interface Config<T> {

	public Class<T> getReferredClass();
	
	public <K> ParamConfig<K> findParamByPath(String path);
	public <K> ParamConfig<K> findParamByPath(String[] pathArr);

	default boolean isMapped() {
		return false;
	}
	
	default public MappedConfig<T, ?> findIfMapped() {
		return null;
	}

	public interface MappedConfig<T, M> extends Config<T> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default public MappedConfig<T, ?> findIfMapped() {
			return this;
		}
		
		public Config<M> getMapsTo();
	}
}
