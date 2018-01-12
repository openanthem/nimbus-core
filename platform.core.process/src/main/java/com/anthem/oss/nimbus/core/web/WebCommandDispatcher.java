/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class WebCommandDispatcher {

	private final WebCommandBuilder builder;

	private final CommandExecutorGateway gateway;

	public WebCommandDispatcher(BeanResolverStrategy beanResolver) {
		this.builder = beanResolver.get(WebCommandBuilder.class);
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
	}
	
	public Object handle(HttpServletRequest httpReq, RequestMethod httpMethod, ModelEvent<String> event) {
		Command cmd = builder.build(httpReq, event);
		return handle(cmd, event.getPayload());
	}

	public Object handle(HttpServletRequest httpReq, RequestMethod httpMethod, String v, String json) {
		Command cmd = builder.build(httpReq);
		return handle(cmd, json);
	}

	public MultiOutput handle(Command cmd, String payload) {
		return gateway.execute(cmd, payload);
	}

}
