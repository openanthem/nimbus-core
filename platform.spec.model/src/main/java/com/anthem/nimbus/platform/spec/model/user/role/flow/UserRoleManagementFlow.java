/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.role.flow;

import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
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
@ViewDomain("userrole") 
@ViewConfig @MapsTo.Model(ClientUserRole.class) 
@Execution.Input.Default @Execution.Output.Default @Execution.Output({Action._new, Action._nav, Action._process})
@Getter @Setter
public class UserRoleManagementFlow {

	@Page
	private Page_SearchUserRole pg1;

	@Page
	private Page_AddUserRole pg2;
	
}
