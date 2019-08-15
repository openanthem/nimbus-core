/**
 *  Copyright 2016-2019 the original author or authors.
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
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.collections.MapUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement;
import com.antheminc.oss.nimbus.domain.cmd.RefId;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.repo.AbstractWSModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ExampleSearchCriteria;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO: currently an open issue in spring-cloud-netflix:
 * https://github.com/spring-cloud/spring-cloud-netflix/issues/1047 Once update
 * is available, uncomment the feign client and remove direct restTemplate
 * dependency.
 * 
 * In progress: Not all methods of ModelRepository are implemented yet, will be
 * implemented as needed
 * 
 * @author Rakesh Patel
 */
@ConfigurationProperties(prefix="nimbus.ext.repository")
@Getter @Setter
@EnableLoggingInterceptor
public class DefaultWSModelRepository extends AbstractWSModelRepository {

	public static final JustLogit LOG = new JustLogit(DefaultWSModelRepository.class);
	private Map<String, String> targetUrl;

	public DefaultWSModelRepository(BeanResolverStrategy beanResolver, RestTemplate restTemplate) {
		super(beanResolver, restTemplate);
	}

	@Override
	public <T> T _get(Command cmd, ModelConfig<T> mConfig) {
		RefId<?> refId = cmd.getRefId(CommandElement.Type.DomainAlias);
		if (null != refId) {
			LOG.warn(() -> "A refId of \"" + refId + "\" was provided for a _get on alias \"" + mConfig.getAlias()
					+ "\". DefaultWSModelRepository does explicitely handle external object retrieval by using the refId. Consider removing refId.");
		}
		URI uri = createUriForAlias(mConfig.getAlias(), null);
		return handleGet(mConfig.getReferredClass(), uri);
	}

	@Override
	public String getTargetUrl(String alias) {
		if (MapUtils.isEmpty(this.getTargetUrl())) {
			return null;
		}
		return this.getTargetUrl().get(alias);
	}

	@Override
	public <T> T handleGet(Class<?> referredClass, URI uri) {
		ResponseEntity<T> responseEntity = (ResponseEntity<T>) getRestTemplate()
				.exchange(new RequestEntity<T>(HttpMethod.GET, uri), referredClass);
		return Optional.ofNullable(responseEntity).map((response) -> response.getBody()).orElse(null);
	}

	@Override
	public <T> Object handleSearch(Class<?> referredDomainClass, Supplier<SearchCriteria<?>> criteriaSupplier,
			URI uri) {
		SearchCriteria<?> searchCriteria = criteriaSupplier.get();
		Object response = execute(() -> new RequestEntity<Object>(
				searchCriteria instanceof ExampleSearchCriteria ? searchCriteria.getWhere() : null, HttpMethod.POST,
				uri), () -> new ParameterizedTypeReference<List<T>>() {
					public Type getType() {
						return new CustomParameterizedTypeImpl((ParameterizedType) super.getType(),
								new Type[] { referredDomainClass });
					}
				});
		return response;
	}

	@Override
	protected <T> T handleDelete(URI uri) {
		throw new UnsupportedOperationException("_delete operation is not supported for Database.rep_ws repository");
	}

	@Override
	protected <T> T handleNew(Class<?> referredClass, URI uri) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}

	@Override
	protected <T> T handleUpdate(T state, URI uri) {
		throw new UnsupportedOperationException("_update operation is not supported for Database.rep_ws repository");
	}

}
