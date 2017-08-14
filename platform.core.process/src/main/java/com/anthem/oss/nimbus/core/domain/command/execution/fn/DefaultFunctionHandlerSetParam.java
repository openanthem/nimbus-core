/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultFunctionHandlerSetParam<T, R> extends AbstractFunctionHandler<T, R> {

	@Override
	public R execute(ExecutionContext eCtx, Param<T> actionParameter) {
		return null;
	}
}
