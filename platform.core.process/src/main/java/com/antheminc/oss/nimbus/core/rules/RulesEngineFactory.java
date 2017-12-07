/**
 * 
 */
package com.antheminc.oss.nimbus.core.rules;

import com.antheminc.oss.nimbus.core.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.RulesRuntime;

/**
 * @author Soham Chakravarti
 *
 */
public interface RulesEngineFactory {

	public RulesConfig createConfig(String alias);
	
	public RulesRuntime createRuntime(RulesConfig config);
}
