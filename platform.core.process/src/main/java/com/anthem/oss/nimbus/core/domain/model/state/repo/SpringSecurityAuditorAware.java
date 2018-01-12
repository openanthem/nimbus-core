/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;

import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * This class is used to provide the auditor information (currently it is a user name set during the authentication) for the spring auditing fields {@link CreatedBy} and {@link LastModifiedBy}
 * TODO if the authentication implementation is changed (e.g. store the whole user object instead of just the login name), this class needs to be updated as well
 * 
 * @author Rakesh Patel
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		
		ClientUser loggedInUser = UserEndpointSession.getStaticLoggedInUser();
		if(loggedInUser != null) {
			return loggedInUser.getLoginId();
		}
		
		return null;		
	}	
}
