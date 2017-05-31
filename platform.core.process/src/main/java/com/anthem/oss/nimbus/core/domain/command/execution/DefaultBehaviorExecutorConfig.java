/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultBehaviorExecutorConfig {} /*extends AbstractProcessTaskExecutor {

	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> quad = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		return (R)quad.getView();
	}
}
*/