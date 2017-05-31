/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
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
	@Model.Param.Values(url="staticCodeValue-/clientType")
    private Type type;
	
	private String code;

	@NotNull
	private String name;
	
	@NotNull
	@Model.Param.Values(url="staticCodeValue-/orgStatus")
	private Status status;
	
	private String description;
	
	private Date effectiveDate;
	
	private Date terminationDate;

	@Ignore private Set<ClientEntity> nestedEntities;
	
	
	//@Relationship

	@Ignore private Set<ClientAccessEntity> selectedAccesses;
	
	private Address.IdString address;
	
	@Ignore private Set<ClientUserRole> associatedRoles;
	
	
	/**
	 * 
	 * @param ce
	 */
	public void addNestedEntities(ClientEntity ce) {
		if(getNestedEntities() == null) {
			setNestedEntities(new HashSet<>());
		}
		getNestedEntities().add(ce);
	}
	
	/**
	 * 
	 * @param cae
	 */
	public void addSelectedAccess(ClientAccessEntity cae) {
		if(getSelectedAccesses() == null) {
			setSelectedAccesses(new HashSet<>());
		}
		getSelectedAccesses().add(cae);
	}
	
	/**
	 * 
	 */
	public void addClientUserRole(ClientUserRole cr){
		if(getAssociatedRoles() == null){
			setAssociatedRoles(new HashSet<>());
		}
		getAssociatedRoles().add(cr);
	}
	
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = Date.from(effectiveDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public LocalDate getEffectiveDate() {
		if(this.effectiveDate != null) {
			return Instant.ofEpochMilli(this.effectiveDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}
	
	public void setTerminationDate(LocalDate terminationDate) {
		this.terminationDate = Date.from(terminationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public LocalDate getTerminationDate() {
		if(this.terminationDate != null) {
			return Instant.ofEpochMilli(this.terminationDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}
	
}
