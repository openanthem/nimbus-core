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
package com.antheminc.oss.nimbus.domain.model.state.repo.ws;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.GenericExecute;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.GenericListExecute;
import com.antheminc.oss.nimbus.domain.model.state.repo.AbstractWSModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 * @author Rakesh Patel
 *
 * TODO:
 * 	1. Rakesh/Swetha: Need to account for when the output contains ExecuteException where result would be null.
 *
 */
@ConfigurationProperties(prefix="remote.repository")
@Getter @Setter
@SuppressWarnings("unchecked")
public class RemoteWSModelRepository extends AbstractWSModelRepository {
	
	private Map<String, String> serviceUrl;
	
	public RemoteWSModelRepository(BeanResolverStrategy beanResolver, RestTemplate restTemplate) {
		super(beanResolver, restTemplate);	
	}
	
	@Override
	protected <T> T handleNew(Class<?> referredClass, URI _newUri) {
		Object response = execute(() -> new RequestEntity<GenericExecute<T>>(HttpMethod.GET, _newUri), 
				() -> createGenericRespEntity(referredClass));
		
		GenericExecute<T> output = (GenericExecute<T>) response;
		
		return output.extractSingleValue();
	}
	
	@Override
	public <T> T handleGet(Class<?> referredClass, URI uri) {
		Object response = execute(() -> new RequestEntity<GenericExecute<T>>(HttpMethod.GET, uri), 
				() -> createGenericRespEntity(referredClass));
		
		GenericExecute<T> output = (GenericExecute<T>) response;
		return output.extractSingleValue();
	}
	
	@Override
	public <T> Object handleSearch(Class<?> referredDomainClass, Supplier<SearchCriteria<?>> criteriaSupplier, URI uri) {
		SearchCriteria<?> searchCriteria = criteriaSupplier.get();
		
		Object criteria = searchCriteria.getWhere() != null ? (String) searchCriteria.getWhere() : "{}";
		
		RequestEntity<Object> request = (RequestEntity<Object>) RequestEntity
			     .post(uri)
			     .contentType(MediaType.APPLICATION_JSON)
			     .body(criteria);
		
		if (StringUtils.isEmpty(searchCriteria.getFetch())) {			
			Object response = execute(() -> request, 
					() -> createGenericListRespEntity(referredDomainClass));
			GenericListExecute<T> output = (GenericListExecute<T>) response;
			return output.extractSingleValue();	
			
		} else {			
			Object response = execute(() -> request, 
					() -> createGenericRespEntity(referredDomainClass));
			
			GenericExecute<T> output = (GenericExecute<T>) response;
			return output.extractSingleValue();			
		}		
	}
	
	@Override
	protected <T> T handleUpdate(T state, URI _updateUri) {
		RequestEntity<T> request = (RequestEntity<T>) RequestEntity
			     .post(_updateUri)
			     .contentType(MediaType.APPLICATION_JSON)
			     .body(state);
		
		Object response = execute(() -> request, 
				() -> createGenericRespEntity(Boolean.class));
		
		GenericExecute<T> output = (GenericExecute<T>) response;
		return output.extractSingleValue();
	}
	
	@Override
	protected <T> T handleDelete(URI _deleteUri) {
		Object response = execute(() -> new RequestEntity<GenericExecute<T>>(HttpMethod.GET, _deleteUri), 
				() -> createGenericRespEntity(Boolean.class));
		
		GenericExecute<T> output = (GenericExecute<T>) response;
		
		return output.extractSingleValue();
	}
	
	//TODO Rakesh/Swetha: this can be provided differently then per each alias - else for each remote domain alias we are sort of repeating the context url in yml file
	@Override
	public String getTargetUrl(String alias) {	
		if(MapUtils.isEmpty(this.serviceUrl))
			return null;
		
		StringBuilder url = new StringBuilder();
		url.append(StringUtils.isBlank(this.serviceUrl.get(alias)) ? null : this.serviceUrl.get(alias));
		return url.toString();			
	}

	private <T> ParameterizedTypeReference<GenericListExecute<List<T>>> createGenericListRespEntity(Class<T> referredDomainClass) {
		return new ParameterizedTypeReference<GenericListExecute<List<T>>>() {
			public Type getType() {
				return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[] {referredDomainClass});
			}
		};
	}
	
	private <T> ParameterizedTypeReference<GenericExecute<T>> createGenericRespEntity(Class<T> referredDomainClass) {
		return new ParameterizedTypeReference<GenericExecute<T>>() {
			public Type getType() {
				return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(), new Type[] {referredDomainClass});
			}
		};
	}
	
}
