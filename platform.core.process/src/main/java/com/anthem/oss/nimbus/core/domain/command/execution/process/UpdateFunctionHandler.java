/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class UpdateFunctionHandler <T,S> extends URLBasedAssignmentFunctionHandler<T,Void,S> {

	
	@Override
	public Void assign(ExecutionContext executionContext, Param<T> actionParameter, Param<S> targetParameter,
			S state) {
		if(targetParameter.isCollection()) {
			ListParam<S> listParam = targetParameter.findIfCollection();
			listParam.add(state);
		}
		return null;
	}
	
}
