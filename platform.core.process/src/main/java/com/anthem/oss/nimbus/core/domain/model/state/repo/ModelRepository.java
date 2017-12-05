/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
import com.antheminc.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.antheminc.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 * TODO - 08/30/2017: Refactor the methods to accept the argument which is generic enough to get the enough information needed e.g. url, alias etc..  
 */
public interface ModelRepository {

	@Getter @RequiredArgsConstructor
	public enum Aggregation {
		COUNT("count", Holder.class);
		
		public final String alias;
		public final Class<?> type;
		
		@SuppressWarnings("unchecked")
		public <T> Class<T> getType() {
			return (Class<T>)type;
		}
		
		public static Aggregation getByAlias(String alias) {
			return Stream.of(Aggregation.values())
				.filter((aggregation) -> StringUtils.equals(aggregation.getAlias(), alias))
				.findFirst()
				.orElse(null);
		}
	}
	
	/*SOHAM: f.w upgrade v0.3: START */
	
	public <T> T _new(ModelConfig<T> mConfig);
	public <T> T _new(ModelConfig<T> mConfig, T newState);
	
	/*SOHAM: f.w upgrade v0.3: END */
	
	// internally used, not exposed as an Action
	default public <ID extends Serializable, T> T _save(String alias, T state) {
		return state;
	}
	
	//Action._get
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias);
	default public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias, String url) {
		return this._get(id, referredClass, alias);
	}
	
	//Action._info
	
	//Action._update: partial update
	public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state);
	
	//Action._replace: complete update
	public void _replace(Param<?> param);
	public void _replace(List<Param<?>> params);
	public <T> T _replace(String alias, T state);
	
	//Action._delete
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias);
		
	public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria);
	
	public default <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria, String url) {
		return this._search(referredDomainClass, alias, criteria);
	}
	
	public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria);
	
	public default <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria, String url) {
		return this._search(referredDomainClass, alias, criteria);
	}
	
	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria);
	
	public default <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria, String url) {
		return this._search(referredDomainClass, alias, criteria);
	}
	

}
