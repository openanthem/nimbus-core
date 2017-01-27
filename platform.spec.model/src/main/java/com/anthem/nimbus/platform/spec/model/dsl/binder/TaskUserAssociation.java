/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Option;

/**
 * @author Rakesh Patel
 *
 */
@CoreDomain("taskuserassociation")
@Repo(Option.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default
public class TaskUserAssociation extends AbstractModel.IdString {

	private static final long serialVersionUID = 1L;

	private Long currentUserId;
	
	private AssignmentTask currentTask;
	
	// when user acts on the task, based on the tasktracker we can change the status in the assignment task
			
}
