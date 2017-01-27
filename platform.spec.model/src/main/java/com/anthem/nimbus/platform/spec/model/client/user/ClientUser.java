/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.client.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.ogm.annotation.RelationshipEntity;

import com.anthem.nimbus.platform.spec.model.client.Client;
import com.anthem.nimbus.platform.spec.model.client.access.ClientAccessEntity;
import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature.Ignore;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.user.AbstractUser;
import com.anthem.nimbus.platform.spec.model.user.PlatformUser;

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
@CoreDomain(value="clientuser")
@Getter @Setter @ToString(callSuper=true)
public class ClientUser extends AbstractUser<ClientUserRole, ClientUserRole.Entry, ClientAccessEntity> {
	
	private static final long serialVersionUID = 1L;
	

	@Ignore private Client client;

	@Ignore
	private PlatformUser platformUser;
	
	//TODO: Remove variable after code merge
	private String loginName;
	
	@Ignore
	private Set<ClientUserIDS> clientUserIDS;
	
	
	@Override
	public Set<ClientUserRole> getGrantedRoles() {
		return super.getGrantedRoles();
	}
	
	@Override
	public void setGrantedRoles(Set<ClientUserRole> grantedRoles) {
		super.setGrantedRoles(grantedRoles);
	}
	
	
	/**
	 * 
	 * @param source
	 * @param loginName
	 * @param guid
	 */
	public void addClientUserIDS(String source, String loginName, String guid){
		clientUserIDS = (clientUserIDS == null) ? new HashSet<ClientUserIDS>() : clientUserIDS;
		Iterator<ClientUserIDS> iter = clientUserIDS.iterator();
		while(iter.hasNext()){
			ClientUserIDS cid = iter.next();
			if(cid.getSource().equals(source)){
				cid.setLoginName(loginName);
				cid.setIdsGuid(guid);
				return;
			}
		}
		ClientUserIDS cid = new ClientUserIDS(source, loginName, guid);
		clientUserIDS.add(cid);
	}
	
}
