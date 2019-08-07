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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
@EnableLoggingInterceptor
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
		Object response = getExprEval().getValue(exprValue, actionParameter);
		return response;
	}
}
