/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.expr;

import java.util.function.BiFunction;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Soham Chakravarti
 *
 */
public class SpelExpressionEvaluator implements ExpressionEvaluator {

	@Override
	public Object getValue(String exprValue, Object rootObject) {
		return getValue(exprValue, rootObject, (expr, ctx)->expr.getValue(ctx));
	}
	
	@Override
	public <T> T getValue(String exprValue, Object rootObject, Class<T> returnType) {
		return getValue(exprValue, rootObject, (expr, ctx)->expr.getValue(ctx, returnType));
	}
	
	private <R> R getValue(String exprValue, Object rootObject, BiFunction<Expression, StandardEvaluationContext, R> fn) {
		StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
		//==SpelParserConfiguration config = new SpelParserConfiguration(true, true);
		ExpressionParser expressionParser = new SpelExpressionParser();
		
		Expression expression = expressionParser.parseExpression(exprValue);
		R response = fn.apply(expression, context);
		
		return response;
	}
}
