/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
////default._process$execute?fn=_set
public class SetFunctionHandler <T,S> extends URLBasedAssignmentFunctionHandler<T,Void,S> {

	
	@Override
	public Void assign(ExecutionContext executionContext, Param<T> actionParameter, Param<S> targetParameter,
			S state) {
		targetParameter.setState(state);
		return null;
	}
	
}
