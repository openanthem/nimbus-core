/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.client;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.client.access.ClientAccessEntity;
import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature.Ignore;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Option;
import com.anthem.nimbus.platform.spec.model.person.Address;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@CoreDomain(value="cliententity")
@Repo(Option.rep_neo4j)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
@Getter @Setter @ToString(callSuper=true)
public class ClientEntity extends AbstractModel.IdLong {

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
