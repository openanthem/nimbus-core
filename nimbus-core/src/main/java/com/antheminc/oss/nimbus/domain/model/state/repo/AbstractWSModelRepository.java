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
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement;
import com.antheminc.oss.nimbus.domain.cmd.RefId;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.support.RefIdHolder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 * @author Rakesh Patel
 *
 */
@Getter @Setter @RequiredArgsConstructor
public abstract class AbstractWSModelRepository implements ExternalModelRepository {

	private final BeanResolverStrategy beanResolver;
	
	private final RestTemplate restTemplate;
	
	abstract protected String getTargetUrl(String alias);
	
	abstract protected <T> T handleNew(Class<?> referredClass, URI uri);
	
	abstract protected <T> T handleGet(Class<?> referredClass, URI uri);
	
	abstract protected <T> Object handleSearch(Class<?> referredDomainClass, Supplier<SearchCriteria<?>> criteriaSupplier, URI uri);
	
	abstract protected <T> T handleUpdate(T state, URI uri);
	
	abstract protected <T> T handleDelete(URI uri);
	
	@Override
	public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig) {
		final String url = cmd.toRemoteUri(CommandElement.Type.ParamName, Action._new, Behavior.$execute);
		
		URI _newUri = createUriForAlias(mConfig.getAlias(), url);
		
		T state = handleNew(mConfig.getReferredClass(), _newUri);
		RefId<?> refId = cmd.getRefId(CommandElement.Type.DomainAlias);
		
		return new RefIdHolder<>(refId, state);
	}
	
	@Override
	public <T> T _get(Command cmd, ModelConfig<T> mConfig) {
		final String url = cmd.toRemoteUri(CommandElement.Type.ParamName, Action._get, Behavior.$execute);
		
		URI _getUri = createUriForAlias(mConfig.getAlias(), url);
		
		return handleGet(mConfig.getReferredClass(),_getUri);
	}
	
	@Override
	public <T> Object _search(Param<?> param, Supplier<SearchCriteria<?>> criteriaSupplier) {
		SearchCriteria<?> searchCriteria = criteriaSupplier.get();
		String searchUri = searchCriteria.getCmd().getAbsoluteUri();
		URI uri = createUriForAlias(param.getRootDomain().getConfig().getAlias(), searchUri);
		if(uri == null)
			return null;
		
		return handleSearch(param.getRootDomain().getConfig().getReferredClass(), criteriaSupplier, uri);
	}
	
	@Override
	public <T> T _update(Param<?> param, T state) {
		final String url = param.getRootExecution().getRootCommand().toRemoteUri(CommandElement.Type.ParamName, Action._update, Behavior.$execute);
		
		URI _updateUri = createUriForAlias(param.getRootDomain().getConfig().getAlias(), url);
		
		return handleUpdate(state, _updateUri);
	}
	
	@Override
	public <T> T _delete(Param<?> param) {
		final String url = param.getRootExecution().getRootCommand().toRemoteUri(CommandElement.Type.ParamName, Action._delete, Behavior.$execute);
		
		URI _deleteUri = createUriForAlias(param.getRootDomain().getConfig().getAlias(), url);
	
		return handleDelete(_deleteUri);
	}
	
	protected URI createUriForAlias(String alias, String url) {
		String urlToConstruct = getTargetUrl(alias);
		if(StringUtils.isEmpty(urlToConstruct)) {
			return null;
		}
		if (!StringUtils.isEmpty(url)) {
			urlToConstruct = urlToConstruct.concat(url);
		}
		try {
			return new URI(urlToConstruct);
		} catch (URISyntaxException e) {
			throw new FrameworkRuntimeException("Cannot create URI from supplied url: "+url, e);
		}
	}
	
	protected Object execute(Supplier<RequestEntity<?>> reqEntitySupplier, Supplier<ParameterizedTypeReference<?>> responseTypeSupplier) {
		 try {
			ResponseEntity<?> responseEntity = getRestTemplate().exchange(reqEntitySupplier.get(), responseTypeSupplier.get());
			return Optional.of(responseEntity).map((response)->response.getBody()).orElse(null);
		} catch(Exception e) {
			throw throwEx(e, reqEntitySupplier.get().getUrl());
		}
	}
	
	protected class CustomParameterizedTypeImpl implements ParameterizedType {
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
