/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.Set;

import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
import com.anthem.oss.nimbus.core.entity.access.DefaultRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class DefaultUser extends AbstractUser<DefaultRole, DefaultRole.Entry, DefaultAccessEntity> {

	private static final long serialVersionUID = 1L;


	@Override
	public Set<DefaultRole> getGrantedRoles() {
		return super.getGrantedRoles();
	}

	@Override
	public void setGrantedRoles(Set<DefaultRole> grantedRoles) {
		super.setGrantedRoles(grantedRoles);
	}
	
}
