/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
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
@Domain(value="cliententity")
@Repo(Repo.Database.rep_neo4j)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
@Getter @Setter @ToString(callSuper=true)
public class ClientEntity extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;


	public enum Type {
		
        CLIENT,
		
        ORG;
	}
	
	
    private Type type;
	
	private String code;

	@NotNull
	private String name;

	@Ignore private Set<ClientEntity> nestedEntities;
	
	
	//@Relationship

	@Ignore private Set<ClientAccessEntity> selectedAccesses;
	
	private Address.IdLong address;
	
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
	
}
