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

import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.expr.SpelExpressionEvaluator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public class ConfigVariableFunctionHandler<T> implements FunctionHandler<T, Object> {

	public static final String ARG_NAME = "name";
	public static final String ARG_SPEL = "spel";
	public static final String ARG_VALUE = "value";

	private final ReservedKeywordRegistry reservedKeywordRegistry;
	private final SpelExpressionEvaluator expressionEvaluator;
	private final CommandExecutorGateway commandExecutorGateway;

	@Override
	public Object execute(ExecutionContext eCtx, Param<T> actionParameter) {
		validate(eCtx, actionParameter);
		String variableName = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_NAME);
		Object variableValue = determineVariableValue(eCtx, actionParameter);
		eCtx.addVariable(variableName, variableValue);
		return new SimpleEntry<String, Object>(variableName, variableValue);
	}

	private Object determineVariableValue(ExecutionContext eCtx, Param<T> actionParameter) {
		String valueProviderString = getValue(eCtx);
		if (!StringUtils.isEmpty(valueProviderString)) {
			return valueProviderString;
		}

		valueProviderString = getSpel(eCtx);
		if (!StringUtils.isEmpty(valueProviderString)) {
			return getExpressionEvaluator().getValue(valueProviderString, actionParameter);
		}

		throw new InvalidConfigException("Unable to determine variable value.");
	}

	private String getSpel(ExecutionContext eCtx) {
		return eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_SPEL);
	}

	private String getValue(ExecutionContext eCtx) {
		return eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_VALUE);
	}

	/**
	 * <p> Validate that the necessary information has been provided.
	 * 
	 * @param eCtx the execution context
	 * @param actionParameter the action param
	 */
	private void validate(ExecutionContext eCtx, Param<T> actionParameter) {
		if (null == actionParameter) {
			throw new InvalidConfigException("The value provided for actionParameter must be non-null.");
		}

		String name = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_NAME);
		if (StringUtils.isEmpty(name)) {
			throw new InvalidConfigException("The argument \"" + ARG_NAME + "\" is required.");
		}

		if (getReservedKeywordRegistry().exists(name)) {
			throw new InvalidConfigException("The placeholder \"" + name
					+ "\" is a reserved keyword. Replacing reserved placeholders is not allowed.");
		}

		long numProvidedValues = Stream.of(getValue(eCtx), getSpel(eCtx)).filter(StringUtils::isNotEmpty)
				.count();
		if (1 != numProvidedValues) {
			throw new InvalidConfigException("One and only one value is required for either: " + ARG_VALUE + " or "
					+ ARG_SPEL + ".");
		}
	}
}
