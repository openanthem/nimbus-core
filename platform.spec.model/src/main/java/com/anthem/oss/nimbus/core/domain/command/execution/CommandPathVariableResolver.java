/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface CommandPathVariableResolver {

	public String resolve(ExecutionContext eCtx, Param<?> commandParam, String pathToResolve);
}
