/**
 * 
 */
package com.antheminc.oss.nimbus.app.extension.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

/**
 * Default implementation of ClientUserDetails
 * 
 * @author Swetha Vemuri
 *
 */
public class DefaultClientUserDetails implements ClientUserDetails {

	private static final long serialVersionUID = 1L;
	
	private ClientUser clientUser;
	
	public DefaultClientUserDetails(ClientUser clientUser) {
		this.clientUser = clientUser;
	}
	
	/* 
	 * (non-Javadoc)
	 * Returns the clientuser #loginId used to authenticate the user
	 */
	@Override
	public String getUsername() {
		return this.clientUser.getLoginId();
	}

	/* (non-Javadoc)
	 * Returns the authenticated ClientUser.
	 */
	@Override
	public ClientUser getAuthenticatedClientUser() {
		return this.clientUser;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
