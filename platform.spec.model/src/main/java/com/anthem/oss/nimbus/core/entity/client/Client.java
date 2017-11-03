/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="client", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class Client extends ClientEntity {

	private static final long serialVersionUID = 1L;
	
	
	public Client() {
		super.setType(ClientEntity.Type.CLIENT);
	}
	

	@NotNull
	private String fedTaxID;
	
    @NotNull
	//@Model.Param.Values(Values.BusinessType.class)
	@Getter @Setter private String businessType;
	
}
