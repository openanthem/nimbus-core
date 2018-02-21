package com.antheminc.oss.nimbus.domain.cmd.exec;


/**
 * @author Rakesh Patel
 *
 */
public interface ExecutionContextPathVariableResolver {

	public String resolve(ExecutionContext eCtx, String pathToResolve);
}
