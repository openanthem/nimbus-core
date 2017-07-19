/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface RulesRuntime {

	public RulesConfig getRulesConfig();
	
	public void start();
	
	public void fireRules(Param<?> param);
	
	public void shutdown();
	
	public <S> S unwrap(Class<S> clazz);
}
