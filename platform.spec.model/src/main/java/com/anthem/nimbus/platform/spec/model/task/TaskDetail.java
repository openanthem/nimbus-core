/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.model.config.StaticValues;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */
@Domain(value="taskDetail")
@Execution.Input.Default @Execution.Output.Default @Execution.Output(Action._new)
@Getter @Setter
public class TaskDetail extends AbstractEntity.IdLong  {
	
	private static final long serialVersionUID = 1L;
	

	@Model.Param.Values(StaticValues.YesNo.class)
	private String hipaaVerified;

	private String attemptNo;

	private String attemptDate;

	@Model.Param.Values(StaticValues.YesNo.class)
	private String patientConsent;

}
