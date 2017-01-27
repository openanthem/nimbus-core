/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
//import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo.Option;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Mantha
 *
 */
@CoreDomain("task")
@Repo(Option.rep_mongodb)
@Getter @Setter @ToString
@Execution.Input.Default @Execution.Output.Default
public class Task extends AbstractModel.IdString  {
	
	private static final long serialVersionUID = 1L;
	

	private String taskId;

	private String taskName;

	private String status;
	
}
