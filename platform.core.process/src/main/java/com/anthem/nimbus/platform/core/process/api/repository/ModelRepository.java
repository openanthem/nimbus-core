/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.io.Serializable;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelRepository {

	//==public <T, R> R doExecute(CommandMessage cmdMsg, T input);
	
	//==public <T> void doExecute(ModelEvent<ParamStateAndConfig<T>> event);
	
	//Action._new
	//==public <T> T _new(CommandMessage cmdMsg, T input);
	public <T> T _new(Class<T> referredClass, String alias);
	public <T> T _new(Class<T> referredClass, String alias, T input);
	
	
	//Action._get
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias);
	
	//Action._info
	
	//Action._update: partial update
	public <T> T _update(String alias, String id, String path, T state);
	
	//Action._replace: complete update
	public void _replace(StateAndConfig.Param<?> param);
	public void _replace(List<StateAndConfig.Param<?>> params);
	public <T> T _replace(String alias, T state);
	
	//Action._delete
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias);
		
	//Action._search
	public <T, C> List<T> _search(Class<T> referredClass, String alias, C criteria);
	
}
