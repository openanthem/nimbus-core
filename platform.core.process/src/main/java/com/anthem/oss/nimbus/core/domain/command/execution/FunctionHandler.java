/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.HierarchyMatch;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface FunctionHandler<T, R> extends HierarchyMatch{
	
	public R execute(ExecutionContext eCtx, Param<T> actionParameter);
	
}
