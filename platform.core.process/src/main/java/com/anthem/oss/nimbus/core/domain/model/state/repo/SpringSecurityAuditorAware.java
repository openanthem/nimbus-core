/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import java.util.Optional;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class is used to provide the auditor information (currently it is a user name set during the authentication) for the spring auditing fields {@link CreatedBy} and {@link LastModifiedBy}
 * TODO if the authentication implementation is changed (e.g. store the whole user object instead of just the login name), this class needs to be updated as well
 * 
 * @author Rakesh Patel
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return Optional.ofNullable(authentication).map((auth) ->  (String) auth.getPrincipal()).orElse("");
	    
	 }
	
}
