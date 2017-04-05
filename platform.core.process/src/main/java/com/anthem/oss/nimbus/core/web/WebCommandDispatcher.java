/**
 *
 */
package com.anthem.oss.nimbus.core.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;

/**
 * @author Soham Chakravarti
 *
 */
public class WebCommandDispatcher {

	WebCommandBuilder builder;

	ProcessGateway processGateway;

	public WebCommandDispatcher(WebCommandBuilder builder,ProcessGateway processGateway) {
		this.builder = builder;
		this.processGateway = processGateway;
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

	/**
	 *
	 * @param cmd
	 * @param payload
	 * @return
	 */
	public Object handle(Command cmd, String payload) {
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		cmdMsg.setRawPayload(payload);
		try {
			Object resp = processGateway.startProcess(cmdMsg);
			return resp;
		} catch (Exception ex) {
			throw new FrameworkRuntimeException(ex);
		}
	}

}
