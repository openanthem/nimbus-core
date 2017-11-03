/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

/**
 * @author Soham Chakravarti
 *
 */
public interface RulesConfig {
	
	public String getPath();
	
	public <R> R unwrap(Class<R> clazz);
}
