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
package com.antheminc.oss.nimbus.domain.model.state.multitenancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.AntPathMatcher;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.state.multitenancy.MultitenancyProperties.TenantDetail;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>A default {@link TenantRepository} implementation that reads tenant
 * information from application properties.
 * 
 * @author Tony Lopez
 * @since 2.0
 *
 */
@Getter
@Setter
public class DefaultTenantRepository implements TenantRepository {

	private final MultitenancyProperties multitenancyProperties;
	private final AntPathMatcher pathMatcher;

	public DefaultTenantRepository(MultitenancyProperties multitenancyProperties) {
		this.multitenancyProperties = multitenancyProperties;
		this.pathMatcher = new AntPathMatcher();
	}

	@Override
	public Tenant findById(Long id) {
		if (null == id || MapUtils.isEmpty(this.multitenancyProperties.getTenants())) {
			return null;
		}
		return this.toTenant(id, this.multitenancyProperties.getTenants().get(id));
	}
	
	@Override
	public Set<Tenant> findByIds(Set<Long> ids) {
		if (CollectionUtils.isEmpty(ids) || MapUtils.isEmpty(this.multitenancyProperties.getTenants())) {
			return new HashSet<>();
		}
		Set<Tenant> tenants = new HashSet<>();
		ids.stream().map(this::findById).forEach(tenants::add);
		return tenants;
	}
	
	@Override
	public Tenant findOneMatchingPrefix(String value) {
		List<Tenant> matchingTenants = new ArrayList<>();
		for (Entry<Long, TenantDetail> entry : this.multitenancyProperties.getTenants().entrySet()) {
			final String pathToMatch = entry.getValue().getPrefix();
			if (StringUtils.isEmpty(pathToMatch)) {
				throw new InvalidConfigException(
						"Tenant prefix must be defined but was not. Tenant details: " + entry.getValue());
			}
			if (this.pathMatcher.match(pathToMatch, value)) {
				matchingTenants.add(this.toTenant(entry.getKey(), entry.getValue()));
			}
		}

		if (CollectionUtils.isEmpty(matchingTenants)) {
			return null;
		}

		// TODO find best match
		return matchingTenants.get(0);
	}

	private Tenant toTenant(Long id, TenantDetail tenantDetail) {
		Tenant tenant = new Tenant();
		BeanUtils.copyProperties(tenantDetail, tenant);
		tenant.setId(id);
		return tenant;
	}
}
