/**
 * 
 */
package com.anthem.nimbus.platform.core.process.handler;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface FunctionHandler extends HierarchyMatch{
	public <R> R executeProcess(ExecutionContext executionContext, Param<?> actionParameter);
}
