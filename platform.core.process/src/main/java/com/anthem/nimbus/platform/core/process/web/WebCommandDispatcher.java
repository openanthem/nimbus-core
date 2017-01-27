/**
 *
 */
package com.anthem.nimbus.platform.core.process.web;

import com.anthem.nimbus.platform.spec.contract.process.ProcessGateway;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.anthem.nimbus.platform.utils.converter.WebCommandBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class WebCommandDispatcher {

	@Autowired WebCommandBuilder builder;

	@Autowired @Qualifier("default.processGateway")
	ProcessGateway processGateway;

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
			throw new PlatformRuntimeException(ex);
		}
	}

}
