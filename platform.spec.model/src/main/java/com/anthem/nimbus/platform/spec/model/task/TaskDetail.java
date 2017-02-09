/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;
import com.anthem.nimbus.platform.spec.model.dsl.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */
@Domain(value="taskDetail")
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
@Getter @Setter
public class TaskDetail extends AbstractModel.IdLong  {
	
	private static final long serialVersionUID = 1L;
	

	@Model.Param.Values(Values.YesNo.class)
	private String hipaaVerified;

	private String attemptNo;

	private String attemptDate;

	@Model.Param.Values(Values.YesNo.class)
	private String patientConsent;

}
