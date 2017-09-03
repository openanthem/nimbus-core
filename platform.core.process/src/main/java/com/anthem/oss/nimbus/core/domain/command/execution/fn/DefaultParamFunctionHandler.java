/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultParamFunctionHandler<T> implements FunctionHandler<T, Object> {

	public DefaultParamFunctionHandler(BeanResolverStrategy beanResolver) {
		//this.converter = beanResolver.get(CommandMessageConverter.class);
	}

	@Override
	public Object execute(ExecutionContext eCtx, Param<T> actionParameter) {
		String exprValue = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_FN_PARAM_ARG_EXPR.code);
		
		// return self param if no operation is provided
		if(StringUtils.trimToNull(exprValue)==null)
			return actionParameter;
		
		// expression
		StandardEvaluationContext context = new StandardEvaluationContext(actionParameter);
		//==SpelParserConfiguration config = new SpelParserConfiguration(true, true);
		ExpressionParser expressionParser = new SpelExpressionParser();
		
		Expression expression = expressionParser.parseExpression(exprValue);
		Object response = expression.getValue(context);
		
		return response;
	}
}
