/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface FunctionHandler<T, R> extends HierarchyMatch{
	
	public R execute(ExecutionContext executionContext, Param<T> actionParameter);
	
}
