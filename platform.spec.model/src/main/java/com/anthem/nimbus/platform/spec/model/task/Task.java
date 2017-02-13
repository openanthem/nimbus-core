/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import com.anthem.oss.nimbus.core.domain.Domain;
import com.anthem.oss.nimbus.core.domain.Execution;
import com.anthem.oss.nimbus.core.domain.Repo;
import com.anthem.oss.nimbus.core.domain.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

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
public class Task extends AbstractEntity.IdString  {
	
	private static final long serialVersionUID = 1L;

	private String taskId;

	private String taskName;

	private String status;
	
}
