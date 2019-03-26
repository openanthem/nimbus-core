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

import org.springframework.util.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public class ConfigVariableFunctionHandler<T> implements FunctionHandler<T, Object> {

	public static final String ARG_CONFIG = "config";
	public static final String ARG_NAME = "name";
	public static final String ARG_SPEL = "spel";
	public static final String ARG_VALUE = "value";

	private final ReservedKeywordRegistry reservedKeywordRegistry;
	
	@Override
	public Object execute(ExecutionContext eCtx, Param<T> actionParameter) {
		validate(eCtx, actionParameter);
		String variableName = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_NAME);
		Object variableValue = determineVariableValue(eCtx, actionParameter);
		return new SimpleEntry<String, Object>(variableName, variableValue);
	}
	
	private Object determineVariableValue(ExecutionContext eCtx, Param<T> actionParameter) {
		String instruction = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_VALUE);
		if (!StringUtils.isEmpty(instruction)) {
			return instruction;
		}
		
		instruction = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_SPEL);
		if (!StringUtils.isEmpty(instruction)) {
			// TODO
		}
		
		instruction = eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_CONFIG);
		if (!StringUtils.isEmpty(instruction)) {
			// TODO
		}
		
		return null;
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

		if (StringUtils.isEmpty(eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_VALUE))) {
			if (StringUtils.isEmpty(eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_SPEL))) {
				if (StringUtils.isEmpty(eCtx.getCommandMessage().getCommand().getFirstParameterValue(ARG_CONFIG))) {
					throw new InvalidConfigException("At least one value is required for either: " + ARG_VALUE + ", "
							+ ARG_SPEL + ", or " + ARG_CONFIG + ".");
				}
			}
		}
	}
}
