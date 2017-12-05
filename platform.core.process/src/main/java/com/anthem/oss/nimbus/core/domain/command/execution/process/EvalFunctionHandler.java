/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import org.activiti.engine.impl.el.ExpressionManager;

import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class EvalFunctionHandler<T,R> implements FunctionHandler<T, R> {
	
	private ExpressionManager expressionManager;
	
	public EvalFunctionHandler(ExpressionManager expressionManager){
		this.expressionManager = expressionManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		String expressionToEvaluate = getExpressionToEvaluate(executionContext);
		return (R)expressionManager.createExpression(expressionToEvaluate).getValue(null);
	}
	
	
	private String getExpressionToEvaluate(ExecutionContext executionContext){
		return executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_EXECUTE_EVAL_ARG.code);
	}
}
