/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.io.Serializable;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelRepository {

	@Getter @RequiredArgsConstructor
	public enum Projection {
		COUNT("count", Long.class);
		
		public final String alias;
		public final Class<?> type;
		
		@SuppressWarnings("unchecked")
		public <T> Class<T> getType() {
			return (Class<T>)type;
		}
	}
	
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
	public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state);
	
	//Action._replace: complete update
	public void _replace(Param<?> param);
	public void _replace(List<Param<?>> params);
	public <T> T _replace(String alias, T state);
	
	//Action._delete
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias);
		
	//Action._search
	public <T, C> List<T> _search(Class<T> referredDomainClass, String alias, C criteria);
	
	default <T, C> T _search(Class<?> referredDomainClass, String alias, C criteria, Projection projection) {
		return _search(referredDomainClass, alias, criteria, projection.getType());
	}
	
	@SuppressWarnings("unchecked")
	default <T, C> T _search(Class<?> referredDomainClass, String alias, C criteria, Class<T> projection) {
		return (T)_search(referredDomainClass, alias, criteria);
	}
}
