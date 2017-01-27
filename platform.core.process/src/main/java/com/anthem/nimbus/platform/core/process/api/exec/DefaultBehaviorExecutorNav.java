/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default.$nav")
public class DefaultBehaviorExecutorNav extends DefaultActionExecutorNav {

	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		String navigationDirection = cmdMsg.getRawPayload();
		return (R)findNavigationPage(q, navigationDirection);
	}

}
