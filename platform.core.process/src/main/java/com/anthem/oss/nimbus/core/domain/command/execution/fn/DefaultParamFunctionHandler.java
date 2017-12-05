/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.fn;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultParamFunctionHandler<T> implements FunctionHandler<T, Object> {

	private final ExpressionEvaluator exprEval;
	
	public DefaultParamFunctionHandler(BeanResolverStrategy beanResolver) {
		this.exprEval = beanResolver.get(ExpressionEvaluator.class);
	}

	@Override
	public Object execute(ExecutionContext eCtx, Param<T> actionParameter) {
		String exprValue = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_FN_PARAM_ARG_EXPR.code);
		
		// return self param if no operation is provided
		if(StringUtils.trimToNull(exprValue)==null)
			return actionParameter;
		
		// expression
		Object response = exprEval.getValue(exprValue, actionParameter);
		return response;
	}
}
