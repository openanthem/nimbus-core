/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class AddFunctionHandler<T,S> extends URLBasedAssignmentFunctionHandler<T,Void,S> {

	@Override
	@SuppressWarnings("unchecked")
	public Void assign(ExecutionContext executionContext, Param<T> actionParameter, Param<S> targetParameter,
			S state) {
		targetParameter.findIfCollection().add(state);
		return null;
	}

}
