/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.ws;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.IdSequenceRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultWSModelRepository implements ModelRepository {

	private final RestTemplate restTemplate;
	
	public DefaultWSModelRepository(BeanResolverStrategy beanResolver) {
		this.restTemplate = beanResolver.get(RestTemplate.class);
	}
	
	@Override
	public <T> T _new(ModelConfig<T> mConfig) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <T> T _new(ModelConfig<T> mConfig, T newState) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <ID extends Serializable, T> T _update(String alias, ID id, String path, T state) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public void _replace(Param<?> param) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public void _replace(List<Param<?>> params) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <T> T _replace(String alias, T state) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria, String url) {
		url = "http://localhost:8080"+url; 
		T obj = restTemplate.postForObject(url, criteria.getWhere(), referredDomainClass);
		return obj;
	}

}
