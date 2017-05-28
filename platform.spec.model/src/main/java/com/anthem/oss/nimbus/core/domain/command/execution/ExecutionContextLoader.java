/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionContextLoader {

	public ExecutionContext load(CommandMessage cmdMsg);
}
