/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client.user;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.user.AbstractUser;
import com.anthem.oss.nimbus.core.entity.user.DefaultUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link RelationshipEntity} based implementation which forces to use Neo4j annotations explicitly in the model.<br>
 * This entity also subclasses {@link AbstractUser} which will allow for {@link Client} based data which may be different across other clients and from global {@link PlatformUser}.<br>
 * Associated business logic can choose to only store differences from info in PlatformUser instance and take a merged view for each client giving client entry the priority.<br>
 * <br>
 * TODO: Based on business logic needed, this class can be enhanced to include the merged view behavior as it already refers to PlatformUser<br>
 * 
 * @author Soham Chakravarti
 */
@Domain("clientuser")
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class ClientUser extends AbstractUser<ClientUserRole, ClientUserRole.Entry, ClientAccessEntity> {
	
	private static final long serialVersionUID = 1L;
	
	@Ignore private Client client;

	@Ignore
	private DefaultUser platformUser;
	
	private String roleName; // Temp for header
	
}
