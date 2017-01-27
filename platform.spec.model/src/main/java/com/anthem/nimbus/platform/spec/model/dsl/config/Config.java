/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

/**
 * @author Soham Chakravarti
 *
 */
public interface Config<T> {

	
	/**
	 * 
	 */
	public Class<T> getReferredClass();
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public <K> ParamConfig<K> findParamByPath(String path);
	
	/**
	 * 
	 * @param pathArr
	 * @return
	 */
	public <K> ParamConfig<K> findParamByPath(String[] pathArr);

	/**
	 * 
	 * @param param
	 * @param eventPublisher
	 */
	public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider eventPublisher);
	
}
