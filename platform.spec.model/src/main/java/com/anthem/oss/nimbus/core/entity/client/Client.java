/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("client")
@Repo
@Getter @Setter @ToString(callSuper=true)
public class Client extends ClientEntity {

	private static final long serialVersionUID = 1L;
	
	
	public Client() {
		super.setType(ClientEntity.Type.CLIENT);
	}
	

	@NotNull
	private String fedTaxID;
	
    @NotNull
	@Model.Param.Values(Values.BusinessType.class)
	@Getter @Setter private String businessType;
	
}
