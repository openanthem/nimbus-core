/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.expr;

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
		StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
		//==SpelParserConfiguration config = new SpelParserConfiguration(true, true);
		ExpressionParser expressionParser = new SpelExpressionParser();
		
		Expression expression = expressionParser.parseExpression(exprValue);
		Object response = expression.getValue(context);
		
		return response;
	}
}
