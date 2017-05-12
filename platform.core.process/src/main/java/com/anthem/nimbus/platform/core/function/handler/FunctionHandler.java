/**
 * 
 */
package com.anthem.nimbus.platform.core.function.handler;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface FunctionHandler extends HierarchyMatch{
	public static final String FUNCTION_ID = "fn";
	public <R> R executeProcess(ExecutionContext executionContext, Param<?> actionParameter);
}
