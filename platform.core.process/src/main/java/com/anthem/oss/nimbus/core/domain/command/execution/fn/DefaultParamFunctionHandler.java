/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.fn;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

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
