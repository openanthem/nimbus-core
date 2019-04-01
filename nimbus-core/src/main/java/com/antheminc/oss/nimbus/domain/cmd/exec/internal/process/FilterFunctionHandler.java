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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p> Sets the state of a specified parameter to the result set consisting of
 * the action parameter's collection elements that match the given SpEL
 * predicate. The action parameter must be a collection or array typed
 * parameter.
 * 
 * <p>The expected SpEL predicate syntax is slightly different when the action
 * parameter's collection element type is complex versus when it is not.
 * Non-complex elements will simply expose each collection element's state to
 * the SpEL expression context, while complex elements will expose the entire
 * Param for the collection element. In this way, greater filtering support is
 * available for complex elements.
 * 
 * <p>Unless set via {@code targetParam}, the default behavior is to set the
 * resulting state into the action parameter.
 * 
 * @author Lance Staley
 * @author Tony Lopez
 */
public class FilterFunctionHandler implements FunctionHandler<Object, Param<Object>> {

	public static final String ARG_EXPRESSION = "expr";
	public static final String ARG_TARGET_PARAM = "targetParam";
	public static final String COL_ONLY_MSG = "Filter is only supported for collection or array typed parameters.";

	/**
	 * <p>Adds the context object to the provided list when the given expression
	 * evaluates to {@code true}.
	 * 
	 * @param list the list to add to when the expression is true
	 * @param context the object to add as a context when evaluating the
	 * expression
	 * @param expression the expression to evaluate
	 */
	private void addWhenExpressionIsTrue(List<Object> list, Object context, Expression expression) {
		addWhenExpressionIsTrue(list, context, context, expression);
	}

	/**
	 * <p>Adds the context object to the provided list when the given expression
	 * evaluates to {@code true}.
	 * 
	 * @param list the list to add to when the expression is true
	 * @param context the object to add as a context when evaluating the
	 * expression
	 * @param toAdd the object to add into the list
	 * @param expression the expression to evaluate
	 */
	private void addWhenExpressionIsTrue(List<Object> list, Object context, Object toAdd, Expression expression) {
		StandardEvaluationContext evalContext = new StandardEvaluationContext(context);
		if (expression.getValue(evalContext, Boolean.class)) {
			list.add(toAdd);
		}
	}

	@Override
	public Param<Object> execute(ExecutionContext eCtx, Param<Object> actionParameter) {
		validate(eCtx, actionParameter);

		final Expression expression = new SpelExpressionParser()
				.parseExpression(eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_EXPRESSION));

		final Object filteredState;
		if (null == actionParameter.getState()) {
			filteredState = null;
		} else if (actionParameter.isCollection()) {
			filteredState = getListFilteredState(actionParameter, expression);
		} else if (actionParameter.getType().isArray()) {
			filteredState = getArrayFilteredState(actionParameter, expression);
		} else {
			throw new InvalidConfigException(COL_ONLY_MSG);
		}

		Param<Object> targetParam = getTargetParam(eCtx, actionParameter);
		targetParam.setState(filteredState);

		return targetParam;
	}

	/**
	 * <p> Get the filtered state of the given action param with the provided
	 * SpEL expression by applying that expression to each collection element of
	 * the action param.
	 * 
	 * @param actionParameter the action param
	 * @param expression the expression to evaluate
	 * @return the filtered state
	 */
	private Object[] getArrayFilteredState(Param<Object> actionParameter, Expression expression) {
		List<Object> filtered = new ArrayList<>();
		Object[] array = (Object[]) actionParameter.getState();
		for (Object arrayElement: array) {
			addWhenExpressionIsTrue(filtered, arrayElement, expression);
		}
		Class<?> referredClass = actionParameter.getConfig().getReferredClass();
		Object[] result = (Object[]) Array.newInstance(referredClass, filtered.size());
		for (int i = 0; i < filtered.size(); i++) {
			result[i] = filtered.get(i);
		}
		return result;
	}

	/**
	 * <p> Get the filtered state of the given action param with the provided
	 * SpEL expression by applying that expression to each collection element of
	 * the action param.
	 * 
	 * @param actionParameter the action param
	 * @param spelExpression the expression to evaluate
	 * @return the filtered state
	 */
	@SuppressWarnings("unchecked")
	private List<Object> getListFilteredState(Param<Object> actionParameter, Expression expression) {
		List<Object> result = new ArrayList<>();
		if (actionParameter.isLeaf()) {
			List<Object> collection = (List<Object>) actionParameter.getState();
			for (Object collectionElement: collection) {
				addWhenExpressionIsTrue(result, collectionElement, expression);
			}
		} else {
			actionParameter.traverseChildren(p -> addWhenExpressionIsTrue(result, p, p.getState(), expression));
		}
		return result;
	}

	/**
	 * <p> Get the target param to set the state into. If a {@code targetParam}
	 * path is provided, return the found param relative from the
	 * actionParameter.
	 * 
	 * @param eCtx the execution context
	 * @param actionParameter the action param
	 * @return the targetParam to set
	 */
	private Param<Object> getTargetParam(ExecutionContext eCtx, Param<Object> actionParameter) {
		String targetParamPath = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_TARGET_PARAM);
		if (StringUtils.isEmpty(targetParamPath)) {
			return actionParameter;
		}
		return actionParameter.findParamByPath(targetParamPath);
	}

	/**
	 * <p> Validate that the necessary information has been provided.
	 * 
	 * @param eCtx the execution context
	 * @param actionParameter the action param
	 */
	private void validate(ExecutionContext eCtx, Param<Object> actionParameter) {
		if (null == actionParameter) {
			throw new InvalidConfigException("The value provided for actionParameter must be non-null.");
		}
		
		if (!actionParameter.isCollection() && !actionParameter.getType().isArray()) {
			throw new InvalidConfigException(COL_ONLY_MSG);
		}

		if (StringUtils.isEmpty(eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_EXPRESSION))) {
			throw new InvalidConfigException("The argument \"" + ARG_EXPRESSION + "\" is required for Filter.");
		}
	}
}
