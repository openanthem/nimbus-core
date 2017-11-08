/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateDeserializer;
import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateSerializer;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.entity.person.Address;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="cliententity", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
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
	//@Model.Param.Values(url="Anthem/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/orgType')")
    private Type type;
	
	private String code;

	@NotNull
	private String name;
	
	@NotNull
	//@Model.Param.Values(url="Anthem/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/orgStatus')")
	private Status status;
	
	private String description;
	
	private LocalDate effectiveDate;
	
	private LocalDate terminationDate;

	//@Ignore private ClientEntity parentEntity;
	
	private String parentorganizationId;
	
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
