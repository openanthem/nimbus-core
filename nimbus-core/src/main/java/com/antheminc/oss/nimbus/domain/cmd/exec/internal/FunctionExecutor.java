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

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Jayant Chaudhuri
 *
 */
@EnableLoggingInterceptor
public class FunctionExecutor<T, R> extends AbstractCommandExecutor<R> {

	public FunctionExecutor(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	private String constructFunctionHandlerKey(CommandMessage cmdMsg) {
		StringBuilder key = new StringBuilder();
		String functionName = cmdMsg.getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		String absoluteUri = cmdMsg.getCommand().getAbsoluteAlias();
		absoluteUri = absoluteUri.replaceAll(Constants.SEPARATOR_URI.code, "\\.");
		key.append(absoluteUri).append(".").append(cmdMsg.getCommand().getAction().toString())
				.append(Behavior.$execute.name()).append(Constants.REQUEST_PARAMETER_MARKER.code)
				.append(Constants.KEY_FUNCTION.code).append("=").append(functionName);
		return key.toString();
	}

	private Supplier<InvalidConfigException> handleUnknownFunctionHandler(Input input) {
		CommandMessage commandMessage = input.getContext().getCommandMessage();
		String functionName = commandMessage.getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		StringBuilder errorMessage = new StringBuilder(
				"Unable to locate a function handler to execute from the command message: ");
		errorMessage.append(input.getContext().getCommandMessage()).append("\".\n");
		if (StringUtils.isEmpty(functionName)) {
			errorMessage.append("No value was provided for the query parameter \"").append(Constants.KEY_FUNCTION.code)
					.append("\".");
		} else {
			errorMessage.append("No function handler exists for \"").append(functionName)
					.append("\". Please ensure that:").append("\n (1) -> ")
					.append("The value for the query parameter \"").append(Constants.KEY_FUNCTION.code).append("\" (\"")
					.append(functionName).append("\") is spelled correctly.").append("\n (2) -> ")
					.append("The bean for ").append("\"").append(constructFunctionHandlerKey(commandMessage))
					.append("\"").append(" exists.");
		}
		return () -> new InvalidConfigException(errorMessage.toString());
	}

	protected <H extends FunctionHandler<T, R>> R executeFunctionHandler(Input input, Class<H> handlerClass) {
		ExecutionContext eCtx = input.getContext();
		Param<T> actionParameter = findParamByCommandOrThrowEx(eCtx);
		H processHandler = Optional.ofNullable(getHandler(input.getContext().getCommandMessage(), handlerClass))
				.orElseThrow(this.handleUnknownFunctionHandler(input));
		return processHandler.execute(eCtx, actionParameter);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		R response = (R) executeFunctionHandler(input, FunctionHandler.class);
		return Output.instantiate(input, input.getContext(), response);
	}

	protected <F extends FunctionHandler<?, ?>> F getHandler(CommandMessage commandMessage, Class<F> handlerClass) {
		String functionName = constructFunctionHandlerKey(commandMessage);
		return getHandler(functionName, handlerClass);
	}

	protected <F extends FunctionHandler<?, ?>> F getHandler(String functionName, Class<F> handlerClass) {
		return findMatchingBean(handlerClass, functionName);
	}

}
