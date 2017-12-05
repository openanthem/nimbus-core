/**
 * 
 */
package com.antheminc.oss.nimbus.core;

import java.util.Collection;

import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public interface BeanResolverStrategy {

	public <T> T find(Class<T> type);
	
	public <T> T get(Class<T> type) throws InvalidConfigException;
	
	
	public <T> T find(Class<T> type, String qualifier);
	
	public <T> T get(Class<T> type, String qualifier) throws InvalidConfigException;
	
	public <T> T find(Class<T> type, Class<?>...generics);
	public <T> T get(Class<T> type, Class<?>...generics) throws InvalidConfigException;
	
	public <T> Collection<T> findMultiple(Class<T> type);
	public <T> Collection<T> getMultiple(Class<T> type);
}
