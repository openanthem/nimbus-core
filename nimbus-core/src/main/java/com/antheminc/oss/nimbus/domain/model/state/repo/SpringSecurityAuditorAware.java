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

import java.util.Optional;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

/**
 * This class is used to provide the auditor information (currently it is a user name set during the authentication) for the spring auditing fields {@link CreatedBy} and {@link LastModifiedBy}
 * TODO if the authentication implementation is changed (e.g. store the whole user object instead of just the login name), this class needs to be updated as well
 * 
 * @author Rakesh Patel
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {
	
	private final SessionProvider sessionProvider;

	public SpringSecurityAuditorAware(BeanResolverStrategy beanResolver) {
		this.sessionProvider = beanResolver.get(SessionProvider.class);
	}	
	
	@Override
	public Optional<String> getCurrentAuditor() {
		
		ClientUser loggedInUser = sessionProvider.getLoggedInUser();
		return Optional.ofNullable(loggedInUser).map(ClientUser::getLoginId);
	}	
}