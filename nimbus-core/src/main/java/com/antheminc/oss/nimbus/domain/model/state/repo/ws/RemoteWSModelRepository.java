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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.GenericExecute;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.GenericListExecute;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.repo.AbstractWSModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ExternalModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.DBSearch;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ExampleSearchCriteria;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 *
 */
@ConfigurationProperties(prefix="remote.repository")
@Getter @Setter
@EnableLoggingInterceptor
public class RemoteWSModelRepository extends AbstractWSModelRepository implements ExternalModelRepository {
	
	@Autowired @Qualifier("searchByExample") 
	private DBSearch searchByExample;
	
	@Autowired @Qualifier("searchByQuery") 
	private DBSearch searchByQuery;
	
	private Map<String, String> serviceUrl;
	
	public RemoteWSModelRepository(BeanResolverStrategy beanResolver) {
		super(beanResolver);	
	}
	
	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias, String url) {
		StringBuilder uri = new StringBuilder();
		uri.append(url)
		.append(Constants.REQUEST_PARAMETER_MARKER.code)
		.append(Constants.MARKER_URI_BEHAVIOR.code)
		.append(Constants.PARAM_ASSIGNMENT_MARKER.code)
		.append(Behavior.$state.name());
		
		return super._get(id, referredClass, alias, uri.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <ID extends Serializable, T> T handleGet(ID id, Class<T> referredClass, URI uri) {
		
		Object response;
		response = execute(() -> new RequestEntity<GenericExecute<T>>(HttpMethod.GET, uri), 
				() -> createGenericRespEntity(referredClass));
		
		GenericExecute<T> output = (GenericExecute<T>) response;
		return output.extractSingleValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Object handleSearch(Class<T> referredDomainClass, Supplier<SearchCriteria<?>> criteriaSupplier,String alias, URI uri) {
		SearchCriteria<?> searchCriteria = criteriaSupplier.get();
		
		Object criteria;
		if (searchCriteria instanceof ExampleSearchCriteria) {
			criteria = searchCriteria.getWhere() != null ? (String) searchCriteria.getWhere() : "{}";
		} else {
			criteria = getSearchByQuery().search(referredDomainClass, alias, searchCriteria);
		}
				
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
	public String getTargetUrl(String alias) {	
		if(MapUtils.isEmpty(this.serviceUrl))
			return null;
		
		StringBuilder url = new StringBuilder();
		url.append(StringUtils.isBlank(this.serviceUrl.get(alias)) ? null : this.serviceUrl.get(alias));
		return url.toString();			
	}

	@Override
	public String constructSearchUri(SearchCriteria<?> searchCriteria) {
		StringBuilder url = new StringBuilder();
		url.append(searchCriteria.getCmd().getAbsoluteUri());
		
		if (!searchCriteria.getCmd().containsFunction()) {
			return url.toString();
		} else {
			url.append(Constants.REQUEST_PARAMETER_MARKER.code)
				.append(Constants.KEY_FUNCTION.code)
				.append(Constants.PARAM_ASSIGNMENT_MARKER.code)
				.append(searchCriteria.getCmd().getRequestParams().get(Constants.KEY_FUNCTION.code)[0]);
			
			searchCriteria.getCmd().getRequestParams().entrySet()
				.stream()
				.filter(map -> !Constants.KEY_FUNCTION.code.equalsIgnoreCase(map.getKey()))
				.filter(map -> map.getValue() != null && map.getValue().length > 0)
				.forEach(map -> url.append(Constants.REQUEST_PARAMETER_DELIMITER.code).append(map.getKey()).append(Constants.PARAM_ASSIGNMENT_MARKER.code).append(map.getValue()[0]));
			
			return url.toString();
		}
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
