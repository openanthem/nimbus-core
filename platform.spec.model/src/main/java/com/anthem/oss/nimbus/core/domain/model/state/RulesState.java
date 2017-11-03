/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.model.config.RulesConfig;

/**
 * @author Soham Chakravarti
 *
 */
public interface RulesState {

	public RulesConfig getConfig();

	public void fireRules();
}
