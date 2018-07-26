package com.antheminc.oss.nimbus.domain.cmd.exec;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 *
 */
public interface ExecutionContextPathVariableResolver {

	public String resolve(ExecutionContext eCtx, Param<?> commandParam, String pathToResolve);
}
