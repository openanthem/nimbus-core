/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain("clientusergroup")
@Repo
@Getter @Setter @ToString(callSuper=true)
public class ClientUserGroup extends AbstractUserGroup {

	private static final long serialVersionUID = 1L;
	
	private Set<ClientUser> participants;
	
	private Set<ClientEntity> associatedTo;
	
	@Model.Param.Values(url = "staticCodeValue-/users")
	@Getter @Setter
	private String[] userList;
	
	private List<String> admins;
	/**
	 * 
	 * @param ce
	 */
	public void addassociatedTo(ClientEntity ce) {
		if(getAssociatedTo() == null) {
			setAssociatedTo(new HashSet<>());
		}
		getAssociatedTo().add(ce);
	}
	
}
