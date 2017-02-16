/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client.access;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.util.CollectionUtils;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.entity.access.AccessEntity;
import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
import com.anthem.oss.nimbus.core.entity.access.Permission;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class ClientAccessEntity extends AbstractEntity.IdLong implements AccessEntity {
	
	private static final long serialVersionUID = 1L;

	
	@ReadOnlyProperty
	private DefaultAccessEntity referringAccess;
	
	private Set<ClientAccessEntity> selectedAccesses;
	
	//private Set<Permission> selectedPermissions;
	
	private String code;
	
	private String domainUri; 
	

	public ClientAccessEntity() {}
	
	public ClientAccessEntity(DefaultAccessEntity referringAccess) {
		this.setReferringAccess(referringAccess);
		this.setCode(referringAccess.getCode());
		this.setDomainUri(referringAccess.getDomainUri());
	}
	
	
	/**
	 * 
	 */
	@Override
	public Set<Permission> getPermissions() {
		return referringAccess.getAvailablePermissions();
	}
	
	/**
	 * Verifies that the selectedAccess is a first level child of containing staticAccessEntity and 
	 * if not already present, creates and adds a new instance of ClientAccess.
	 * 
	 * @param proposedAccesses
	 */
	public ClientAccessEntity addSelectedAccess(DefaultAccessEntity...proposedAccesses) {
		if(ArrayUtils.isEmpty(proposedAccesses)) return null;
		
		for(DefaultAccessEntity ae : proposedAccesses) {
			/* exit if already added */
			ClientAccessEntity cae = findInSelectedAccess(ae);
			if(findInSelectedAccess(ae) != null) return cae;
			
			return addSelectedAccessInternal(ae);
		}
		return null;
	}
	
	
	/*
	public void addSelectedPermission(Permission...proposedPermissions) {
		if(ArrayUtils.isEmpty(proposedPermissions)) return;
		
		for(Permission p : proposedPermissions) {
			// exit if already added 
			if(getSelectedPermissions()!=null && getSelectedPermissions().contains(p)) return;
			
			addSelectedPermissionInternal(p);
		}
	}
	
	private void addSelectedPermissionInternal(Permission p) {
		if(getSelectedPermissions() == null) {
			setSelectedPermissions(new HashSet<>());
		}
		//Add the Permission only if it is available at the platform level for the ClientAccessEntity
		if(referringAccess.getAvailablePermissions() != null && referringAccess.getAvailablePermissions().size() > 0) {
			for(Permission availablePrmsn : referringAccess.getAvailablePermissions()){
				if(p.equals(availablePrmsn)){
					//refer back to same static node, not creating a new instance
					getSelectedPermissions().add(availablePrmsn);
				}
			}
		}				
	}
	*/
	
	/**
	 * 
	 * @param ae
	 * @return
	 */
	private ClientAccessEntity addSelectedAccessInternal(DefaultAccessEntity ae) {
		if(getSelectedAccesses() == null) {
			setSelectedAccesses(new HashSet<>());
		}
		ClientAccessEntity cae = new ClientAccessEntity(ae);
		getSelectedAccesses().add(cae);
		return cae;
	}
	
	/**
	 * 
	 * @param ae
	 * @return
	 */
	public ClientAccessEntity findInSelectedAccess(DefaultAccessEntity ae) {
		if(CollectionUtils.isEmpty(getSelectedAccesses())) return null;
		
		for(ClientAccessEntity clientAccess : getSelectedAccesses()) {
			Long casId = clientAccess.getReferringAccess().getId();
			if(casId != null && casId.equals(ae.getId())) {
				return clientAccess;
			}
		}
		
		return null;
	}
	
}
