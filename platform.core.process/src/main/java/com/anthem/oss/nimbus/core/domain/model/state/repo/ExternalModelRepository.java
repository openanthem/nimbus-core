/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import java.io.Serializable;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
public interface ExternalModelRepository extends ModelRepository {

	default public <T> T _new(ModelConfig<T> mConfig){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _new(ModelConfig<T> mConfig, T newState){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <ID extends Serializable, T> T _save(String alias, T state) {
		return state;
	}
	
	default public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	//Action._update: partial update
	default public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state){
		throw new UnsupportedOperationException("_update operation is not supported for Database.rep_ws repository");
	}
	
	//Action._replace: complete update
	default public void _replace(Param<?> param){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	default public void _replace(List<Param<?>> params){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _replace(String alias, T state){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	//Action._delete
	default public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias){
		throw new UnsupportedOperationException("_delete operation is not supported for Database.rep_ws repository");
	}
		
	default public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria){
		throw new UnsupportedOperationException("_search operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria){
		throw new UnsupportedOperationException("_search operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria){
		throw new UnsupportedOperationException("_search operation is not supported for Database.rep_ws repository");
	}
	
	
	
}
