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
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import org.activiti.engine.impl.el.ExpressionManager;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

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
