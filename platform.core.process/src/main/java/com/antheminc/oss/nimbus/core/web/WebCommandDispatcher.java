/**
 *
 */
package com.antheminc.oss.nimbus.core.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;

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
