/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultBehaviorExecutorConfig extends AbstractProcessTaskExecutor {

	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> quad = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		return (R)quad.getView();
	}
}
