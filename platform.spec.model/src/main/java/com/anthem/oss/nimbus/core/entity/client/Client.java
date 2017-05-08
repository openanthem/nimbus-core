/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="client")
@Repo(Repo.Database.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
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
