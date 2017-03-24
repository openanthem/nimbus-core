/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.Set;

import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
public class ClientUserGroup extends AbstractUserGroup {

	private static final long serialVersionUID = 1L;
	
	private Set<ClientUser> participants;
	
	private Set<ClientEntity> associatedTo;
	
	

	
}
