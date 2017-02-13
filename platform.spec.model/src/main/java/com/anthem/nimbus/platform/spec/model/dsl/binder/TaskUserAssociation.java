/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

/**
 * @author Rakesh Patel
 *
 */
@Domain("taskuserassociation")
@Repo(Database.rep_mongodb)
@Execution.Input.Default @Execution.Output.Default
public class TaskUserAssociation extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private Long currentUserId;
	
	private AssignmentTask currentTask;
	
	// when user acts on the task, based on the tasktracker we can change the status in the assignment task
			
}
