/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.Set;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
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
@Domain(value="clientusergroup")
@Repo(Repo.Database.rep_neo4j)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
@Getter @Setter @ToString(callSuper=true)
public class ClientUserGroup extends AbstractUserGroup {

	private static final long serialVersionUID = 1L;
	
	private Set<ClientUser> participants;
	
	private Set<ClientEntity> associatedTo;
	
	//private ClientEntity associatedTo;
	
}
