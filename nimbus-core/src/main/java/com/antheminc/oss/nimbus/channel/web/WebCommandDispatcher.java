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
/**
 *
 */
package com.antheminc.oss.nimbus.channel.web;

import javax.servlet.http.HttpServletRequest;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
@EnableLoggingInterceptor
public class WebCommandDispatcher {

	private final WebCommandBuilder builder;

	private final CommandExecutorGateway gateway;

	public WebCommandDispatcher(BeanResolverStrategy beanResolver) {
		this.builder = beanResolver.get(WebCommandBuilder.class);
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
		
	}
	
	public Object handle(HttpServletRequest httpReq, ModelEvent<String> event) {
		Command cmd = getBuilder().build(httpReq, event);
		return handle(cmd, event.getPayload());
	}

	public Object handle(HttpServletRequest httpReq, String json) {
		Command cmd = getBuilder().build(httpReq);
		return handle(cmd, json);
	}

	public MultiOutput handle(Command cmd, String payload) {
		return getGateway().execute(cmd, payload);
	}
}
