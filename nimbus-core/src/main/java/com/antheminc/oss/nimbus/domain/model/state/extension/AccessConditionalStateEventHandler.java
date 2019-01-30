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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
import com.antheminc.oss.nimbus.entity.user.UserRole;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
public class AccessConditionalStateEventHandler extends EvalExprWithCrudActions<AccessConditional> {
	
	private final SessionProvider sessionProvider;
	
	public AccessConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.sessionProvider = beanResolver.get(SessionProvider.class);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, AccessConditional configuredAnnotation) {
		
		if(Permission.WRITE == configuredAnnotation.p())
			return;
		
		ClientUser user = getSessionProvider().getLoggedInUser();
		
		if(user != null) {
			
			if(!CollectionUtils.isEmpty(user.getRoles())) {
				Set<String> userRoleCodes = user.getRoles().stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
				
				if(configuredAnnotation.containsRoles() != null && configuredAnnotation.containsRoles().length > 0){
					boolean isTrue = userRoleCodes.stream().anyMatch(userRole -> Arrays.asList(configuredAnnotation.containsRoles()).contains(userRole));
					if(isTrue) {	
						handlePermission(configuredAnnotation.p(), onChangeParam);
						return;
					}
				}
				else if(StringUtils.isNotBlank(configuredAnnotation.whenRoles())){
					boolean isTrue = getExpressionEvaluator().getValue(configuredAnnotation.whenRoles(), userRoleCodes, Boolean.class);
					if(isTrue) {
						handlePermission(configuredAnnotation.p(), onChangeParam);
						return;
					}
				}
			}
			
			Set<String> userAuthorities = user.getResolvedAccessEntities().stream().map(ClientAccessEntity::getCode).collect(Collectors.toSet());
			
			if(!CollectionUtils.isEmpty(user.getResolvedAccessEntities())) {
			
				if(configuredAnnotation.containsAuthority() != null && configuredAnnotation.containsAuthority().length > 0){
					boolean isTrue = userAuthorities.stream().anyMatch(userAccessEntity -> Arrays.asList(configuredAnnotation.containsAuthority()).contains(userAccessEntity));
					if(isTrue) {
						handlePermission(configuredAnnotation.p(), onChangeParam);
						return;
					}
				}
				else if(StringUtils.isNotBlank(configuredAnnotation.whenAuthorities())){
					boolean isTrue = getExpressionEvaluator().getValue(configuredAnnotation.whenAuthorities(), userAuthorities, Boolean.class);
					if(isTrue) {
						handlePermission(configuredAnnotation.p(), onChangeParam);
						return;
					}
				}
			}
		}
	}
	
	
	private void handlePermission(Permission p, Param<?> onChangeParam) {
		if(Permission.HIDDEN == p) {
			onChangeParam.setVisible(false);
			onChangeParam.setEnabled(false);
		}
		else if(Permission.READ == p) {
			onChangeParam.setVisible(true);
			onChangeParam.setEnabled(false);
		}
	}
		
}
