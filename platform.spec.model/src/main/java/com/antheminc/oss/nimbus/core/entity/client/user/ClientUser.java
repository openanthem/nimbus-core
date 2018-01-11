package com.antheminc.oss.nimbus.core.entity.client.user;

import java.util.List;

import org.springframework.data.annotation.ReadOnlyProperty;

import com.antheminc.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.client.Client;
import com.antheminc.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.antheminc.oss.nimbus.core.entity.user.AbstractUser;
import com.antheminc.oss.nimbus.core.entity.user.UserRole;
import com.antheminc.oss.nimbus.core.entity.user.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Soham Chakravarti
 * @author Rakesh Patel
 * 
 */
@Domain(value="clientuser", includeListeners={ListenerType.persistence})
@Repo(alias="clientuser", value=Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class ClientUser extends AbstractUser<ClientUserRole> {
	
	private static final long serialVersionUID = 1L;
	
	@Ignore 
	private Client client;

	private List<UserRole> roles;
	private List<UserStatus> userStatuses;
	
	@ReadOnlyProperty
	private List<ClientAccessEntity> resolvedAccessEntities;
	
	private String roleName; // Temp for header

	
}
