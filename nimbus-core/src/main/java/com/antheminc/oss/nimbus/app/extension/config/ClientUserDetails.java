package com.antheminc.oss.nimbus.app.extension.config;

import org.springframework.security.core.userdetails.UserDetails;

import com.antheminc.oss.nimbus.entity.client.user.ClientUser;
/**
 * Provides default interface for implementation of spring security 
 * {@link org.springframework.security.core.userdetails UserDetails}.
 * <br> Applications using the framework must implement this interface to use the framework's 
 * default user Authorization mechanism.
 * <br> 
 * 
 * @author Swetha Vemuri
 *
 */
public interface ClientUserDetails extends UserDetails{
	
	/**
	 * Returns the authenticated clientuser
	 *
	 * @return the clientuser
	 */
	public ClientUser getAuthenticatedClientUser();
	
}
