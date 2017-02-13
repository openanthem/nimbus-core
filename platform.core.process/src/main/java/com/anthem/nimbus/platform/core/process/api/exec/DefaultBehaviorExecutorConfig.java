/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default.$config")
public class DefaultBehaviorExecutorConfig extends AbstractProcessTaskExecutor {

	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> quad = PlatformSession.getOrThrowEx(cmdMsg.getCommand());
		return (R)quad.getView();
	}
}
