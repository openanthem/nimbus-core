/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Soham Chakravarti
 *
 */
public interface ProcessTaskExecutor {

	/**
	 * 
	 * @param cmdMsg
	 * @return
	 */
	public <R> R doExecute(CommandMessage cmdMsg);

}
