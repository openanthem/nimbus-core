/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.support.expr;

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
