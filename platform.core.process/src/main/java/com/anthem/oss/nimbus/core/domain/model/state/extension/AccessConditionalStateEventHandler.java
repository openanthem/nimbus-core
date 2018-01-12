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
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.Permission;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Rakesh Patel
 *
 */
public class AccessConditionalStateEventHandler extends AbstractConditionalStateEventHandler implements OnStateLoadHandler<AccessConditional> {
	
	public AccessConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	public void handle(AccessConditional configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation);
	}

	protected void handleInternal(Param<?> onChangeParam, AccessConditional configuredAnnotation) {
		
		if(Permission.WRITE == configuredAnnotation.p())
			return;
		
		ClientUser user = UserEndpointSession.getStaticLoggedInUser();
		
		if(user != null) {
			
			if(!CollectionUtils.isEmpty(user.getRoles())) {
				Set<String> userRoleCodes = user.getRoles().stream().map(UserRole::getRoleId).collect(Collectors.toSet());
				
				if(configuredAnnotation.containsRoles() != null && configuredAnnotation.containsRoles().length > 0){
					boolean isTrue = userRoleCodes.stream().anyMatch(userRole -> Arrays.asList(configuredAnnotation.containsRoles()).contains(userRole));
					if(isTrue) {	
						handlePermission(configuredAnnotation.p(), onChangeParam);
						return;
					}
				}
				else if(StringUtils.isNotBlank(configuredAnnotation.whenRoles())){
					boolean isTrue = expressionEvaluator.getValue(configuredAnnotation.whenRoles(), userRoleCodes, Boolean.class);
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
					boolean isTrue = expressionEvaluator.getValue(configuredAnnotation.whenAuthorities(), userAuthorities, Boolean.class);
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
