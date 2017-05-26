/**
 *
 */
package com.anthem.oss.nimbus.core.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;

/**
 * @author Soham Chakravarti
 *
 */
public class WebCommandDispatcher {

	WebCommandBuilder builder;

	CommandExecutorGateway gateway;

	public WebCommandDispatcher(WebCommandBuilder builder, CommandExecutorGateway gateway) {
		this.builder = builder;
		this.gateway = gateway;
	}
	
	public Object handle(HttpServletRequest httpReq, RequestMethod httpMethod, ModelEvent<String> event) {
		Command cmd = builder.build(httpReq, event);
		return handle(cmd, event.getPayload());
	}

	public Object handle(HttpServletRequest httpReq, RequestMethod httpMethod, String v, String json) {
		Command cmd = builder.build(httpReq);

//		String loggedIndUser = SecurityContextHolder.getContext().getAuthentication().getName();
//		if (StringUtils.isNotBlank(loggedIndUser)) {
//			cmd.setClientUserId(loggedIndUser);
//			boolean  hasAccess = authorizationApi.hasAccess(cmd);
//			return hasAccess ? handle(cmd, json) : false;
//		}else {
//			throw  new UnauthorizedClientException("No logged in user  in context");
//		}

		return handle(cmd, json);


	}

	public MultiOutput handle(Command cmd, String payload) {
		return gateway.execute(cmd, payload);
	}

}
