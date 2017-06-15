/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.person.Address;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("cliententity")
@Repo
@Getter @Setter @ToString(callSuper=true)
public class ClientEntity extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;


	public enum Type {
		
        CLIENT,
		ORG;
	}
	
	public enum Status {
		
        INACTIVE,
		ACTIVE;
	}
	
	
	@NotNull
	//@Model.Param.Values(url="staticCodeValue-/clientType")
    private Type type;
	
	private String code;

	@NotNull
	private String name;
	
	@NotNull
	//@Model.Param.Values(url="staticCodeValue-/orgStatus")
	private Status status;
	
	private String description;
	
	private Date effectiveDate;
	
	private Date terminationDate;

	@Ignore private ClientEntity parentEntity;
	
	//@Ignore private Set<ClientAccessEntity> selectedAccesses;
	
	private Address.IdString address;
	
	//@Ignore private Set<ClientUserRole> associatedRoles;
	
	/**
	 * 
	 * @param cae
	 */
//	public void addSelectedAccess(ClientAccessEntity cae) {
//		if(getSelectedAccesses() == null) {
//			setSelectedAccesses(new HashSet<>());
//		}
//		getSelectedAccesses().add(cae);
//	}
//	
//	/**
//	 * 
//	 */
//	public void addClientUserRole(ClientUserRole cr){
//		if(getAssociatedRoles() == null){
//			setAssociatedRoles(new HashSet<>());
//		}
//		getAssociatedRoles().add(cr);
//	}
	
}
