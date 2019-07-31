/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.entity.client;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

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
public class ClientEntity extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;


	public enum Type {
		
        CLIENT,
		ORG,
		APP;
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
	
	private Long parentEntity;
	private List<ClientEntity> nestedEntity;
	
	//@Ignore private Set<ClientAccessEntity> selectedAccesses;
	
	//private Address.IdString address;
	
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
