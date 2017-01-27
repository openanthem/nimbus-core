/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user;

import java.util.Set;

import com.anthem.nimbus.platform.spec.model.access.PlatformAccessEntity;
import com.anthem.nimbus.platform.spec.model.access.PlatformRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class PlatformUser extends AbstractUser<PlatformRole, PlatformRole.Entry, PlatformAccessEntity> {

	private static final long serialVersionUID = 1L;


	@Override
	public Set<PlatformRole> getGrantedRoles() {
		return super.getGrantedRoles();
	}

	@Override
	public void setGrantedRoles(Set<PlatformRole> grantedRoles) {
		super.setGrantedRoles(grantedRoles);
	}
	
}
