/**
 * 
 */
package com.anthem.oss.nimbus.core;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public interface BeanResolverStrategy {

	public <T> T find(Class<T> type);
	
	public <T> T get(Class<T> type) throws InvalidConfigException;
	
	
	public <T> T find(Class<T> type, String qualifier);
	
	public <T> T get(Class<T> type, String qualifier) throws InvalidConfigException;
}
