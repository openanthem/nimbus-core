/**
 * 
 */
package com.antheminc.oss.nimbus.domain.bpm;

import java.io.Serializable;

/**
 * @author Rakesh Patel
 *
 */
public interface ProcessRepository {
	
	public <T> T _save(T state, String alias);
	
	public <T> T _update(String alias, Serializable id, String path, T state);
	
	public <T> T _get(Serializable id, Class<T> clazz, String alias);

}
