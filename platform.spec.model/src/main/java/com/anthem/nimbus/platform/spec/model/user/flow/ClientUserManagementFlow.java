/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.flow;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Page;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@ViewDomain("clientuser") 
@ViewConfig @MapsTo.Model(ClientUser.class) 
@Execution.Input.Default @Execution.Output.Default @Execution.Output({Action._new, Action._nav, Action._process})
@Getter @Setter
public class ClientUserManagementFlow {

	@Page
	private Page_SearchUser pg1;

	@Page
	private Page_AddUser pg2;
	
}
