/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.client;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Option;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@CoreDomain(value="client")
@Repo(Option.rep_neo4j)
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
