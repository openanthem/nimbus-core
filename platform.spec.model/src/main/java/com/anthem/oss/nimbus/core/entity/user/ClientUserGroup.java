/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.List;

import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AC67870
 *
 */
@Getter @Setter
public class ClientUserGroup extends AbstractUserGroup {

	private static final long serialVersionUID = 1L;
	
	private List<ClientUser> users;
	
	private Client client;
	
	

	
}
