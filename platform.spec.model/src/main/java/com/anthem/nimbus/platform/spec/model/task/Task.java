/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Mantha
 *
 */
@Domain("task")
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString
@Execution.Input.Default @Execution.Output.Default
public class Task extends AbstractModel.IdString  {
	
	private static final long serialVersionUID = 1L;

	private String taskId;

	private String taskName;

	private String status;
	
}
