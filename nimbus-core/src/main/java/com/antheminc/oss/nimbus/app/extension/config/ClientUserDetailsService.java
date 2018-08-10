package com.antheminc.oss.nimbus.app.extension.config;

import org.springframework.security.core.userdetails.UserDetailsService;
/**
 * This interface extends the spring security core interface 
 * {@link org.springframework.security.core.userdetails.UserDetailsService UserDetailsService}
 * 
 * <br> Concrete implementations of this interface must take care of implementing the interface 
 * methods to set them with valid values. 
 * </br><br>Framework will resolve the concrete implementations using BeanResolverStrategy to 
 * get to the details of the authenticated client user details and can use the implementations to 
 * further provide default authorization strategies.
 * 
 * @author Swetha Vemuri
 *
 */
public interface ClientUserDetailsService extends UserDetailsService {
	
	public Object getAuthenticatedPrincipal();
	
	public ClientUserDetails getClientUserDetails();
	
}
