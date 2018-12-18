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

package com.antheminc.oss.nimbus.domain.model.state.repo;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractWSModelRepository {
	
	private final BeanResolverStrategy beanResolver;
	private final RestTemplate restTemplate;
	private final static JustLogit logit = new JustLogit(AbstractWSModelRepository.class);
	public AbstractWSModelRepository(BeanResolverStrategy resolver) {
		this.beanResolver = resolver;
		this.restTemplate = beanResolver.get(RestTemplate.class);
	}

	abstract public String getTargetUrl(String alias);
	
	abstract public String constructSearchUri(SearchCriteria<?> searchCriteria);
	
	abstract public <ID extends Serializable, T> T handleGet(ID id, Class<T> referredClass, URI url);
	
	abstract public <T> Object handleSearch(Class<T> referredDomainClass, Supplier<SearchCriteria<?>> criteriaSupplier, String alias, URI uri);
	
	protected URI createUriForAlias(String alias, String url) {
		String targetUrl = getTargetUrl(alias);
		if(targetUrl == null)
			return null;
		
		String urlToConstruct = StringUtils.startsWith(url, "/") ? url : "/".concat(url);
		urlToConstruct = targetUrl.concat(urlToConstruct);
		try {
			URI uri = new URI(urlToConstruct);
			return uri;
		} catch (URISyntaxException e) {
			throw new FrameworkRuntimeException("Cannot create URI for ["+urlToConstruct+"]", e);
		}
	}
	
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias, String url) {
		URI uri = createUriForAlias(alias, url);
		if(uri == null)
			return null;

		try {
			return handleGet(id, referredClass, uri);
		} catch(Exception e) {
			throw new FrameworkRuntimeException("Exception occured while making a remote ws call to ["+uri+"] ", e);
		}
	}
	
	public <T> Object _search(Class<T> referredDomainClass, String alias, Supplier<SearchCriteria<?>> criteriaSupplier) {
		SearchCriteria<?> searchCriteria = criteriaSupplier.get();
		String searchUri = constructSearchUri(searchCriteria);
		URI uri = createUriForAlias(alias, searchUri);
		if(uri == null)
			return null;
		
		return handleSearch(referredDomainClass, criteriaSupplier, alias, uri);
	}
	
	protected Object execute(Supplier<RequestEntity<?>> reqEntitySupplier, Supplier<ParameterizedTypeReference<?>> responseTypeSupplier) {
		 try {
			ResponseEntity<?> responseEntity = getRestTemplate().exchange(reqEntitySupplier.get(), responseTypeSupplier.get());
			return Optional.of(responseEntity).map((response)->response.getBody()).orElse(null);
		} catch(Exception e) {
			throw throwEx(e, reqEntitySupplier.get().getUrl());
		}
	}
	
	public class CustomParameterizedTypeImpl implements ParameterizedType {
	    private ParameterizedType delegate;
	    private Type[] actualTypeArguments;

	    public CustomParameterizedTypeImpl(ParameterizedType delegate, Type[] actualTypeArguments) {
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
	
	private FrameworkRuntimeException throwEx(Exception e, URI uri) {
		if(e instanceof HttpStatusCodeException) {
			return new FrameworkRuntimeException("HttpStatusCodeException while making a remote ws call for ["+uri+"] "+((HttpStatusCodeException) e).getResponseBodyAsString(),e);
		} else if(e instanceof RestClientException) {
			return new FrameworkRuntimeException("RestClientException while making a remote ws call for ["+uri+"] ", e);
		}
			return new FrameworkRuntimeException("Exception while making remote a ws call for ["+uri+"] ",e);
	}
}
