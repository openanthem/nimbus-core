/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;

/**
 * @author Rakesh Patel
 *
 */
@Domain("taskuserassociation")
@Repo(Database.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default
public class TaskUserAssociation extends AbstractModel.IdString {

	private static final long serialVersionUID = 1L;

	private Long currentUserId;
	
	private AssignmentTask currentTask;
	
	// when user acts on the task, based on the tasktracker we can change the status in the assignment task
			
}
