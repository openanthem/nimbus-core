/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.ws;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ExternalModelRepository;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO: 
 * currently an open issue in spring-cloud-netflix: https://github.com/spring-cloud/spring-cloud-netflix/issues/1047
 * Once update is available, uncomment the feign client and remove direct restTemplate dependency.
 * 
 * In progress: Not all methods of ModelRepository are implemented yet, will be implemented as needed
 * 
 * @author Rakesh Patel
 */
@ConfigurationProperties(prefix="ext.repository")
@Getter @Setter
public class DefaultWSModelRepository implements ExternalModelRepository {

	private final RestTemplate restTemplate;
	
	private Map<String, String> targetUrl;
	
	//private ExternalModelRepositoryClient externalRepositoryClient;
	
	public DefaultWSModelRepository(BeanResolverStrategy beanResolver) {
		this.restTemplate = beanResolver.get(RestTemplate.class);
		//this.externalRepositoryClient = beanResolver.get(ExternalModelRepositoryClient.class);
	}
	
	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias, String url) {
		URI uri = createUriForAlias(alias, url);
		if(uri == null)
			return null;
		
		ResponseEntity<T> responseEntity = restTemplate.exchange(new RequestEntity<T>(HttpMethod.GET, uri), referredClass);
		return Optional.ofNullable(responseEntity).map((response) -> response.getBody()).orElse(null);
	}
	
	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria, String url) {
		URI uri = createUriForAlias(alias, url);
		if(uri == null)
			return null;
		
		Object response = execute(() -> new RequestEntity<Object>(HttpMethod.POST, uri), 
						() -> new ParameterizedTypeReference<List<T>>() {
								public Type getType() {
									return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[] {referredDomainClass});
								}
						});
		return response;
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria,  String url) {
		URI uri = createUriForAlias(alias, url);
		if(uri == null)
			return null;
		
		Object response = execute(() -> new RequestEntity<Object>(HttpMethod.POST, uri), 
						() -> new ParameterizedTypeReference<List<T>>() {
								public Type getType() {
									return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[] {referredDomainClass});
								}
						});
		return response;
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria, String url) {
		//return this.externalRepositoryClient._search(criteria.getWhere());
		URI uri = createUriForAlias(alias, url);
		if(uri == null)
			return null;
		
		Object response = execute(() -> new RequestEntity<T>(criteria.getWhere(), HttpMethod.POST, uri), 
						() -> new ParameterizedTypeReference<List<T>>() {
				    			public Type getType() {
				    				return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[] {referredDomainClass});
				    			}
						});
		return response;
	}
	
	
	private Object execute(Supplier<RequestEntity<?>> reqEntitySupplier, Supplier<ParameterizedTypeReference<?>> responseTypeSupplier) {
		ResponseEntity<?> responseEntity = restTemplate.exchange(reqEntitySupplier.get(), responseTypeSupplier.get());
		return Optional.of(responseEntity).map((response)->response.getBody()).orElse(null);
	}
	
	private URI createUriForAlias(String alias, String url) {
		
		if(MapUtils.isEmpty(this.targetUrl) || StringUtils.isBlank(this.targetUrl.get(alias)))
			return null;
		
		String urlToConctruct = StringUtils.startsWith(url, "/") ? url : "/".concat(url);
		
		urlToConctruct = this.targetUrl.get(alias).concat(urlToConctruct);
		try {
			URI uri = new URI(urlToConctruct);
			return uri;
		} catch (URISyntaxException e) {
			throw new FrameworkRuntimeException("Cannot crate URI from supplied url: "+url);
		}
	}
	
	
	public class CustomParameterizedTypeImpl implements ParameterizedType {
	    private ParameterizedType delegate;
	    private Type[] actualTypeArguments;

	    CustomParameterizedTypeImpl(ParameterizedType delegate, Type[] actualTypeArguments) {
	        this.delegate = delegate;
	        this.actualTypeArguments = actualTypeArguments;
	    }

	    @Override
	    public Type[] getActualTypeArguments() {
	        return actualTypeArguments;
	    }

	    @Override
	    public Type getRawType() {
	        return delegate.getRawType();
	    }

	    @Override
	    public Type getOwnerType() {
	        return delegate.getOwnerType();
	    }

	}

}
