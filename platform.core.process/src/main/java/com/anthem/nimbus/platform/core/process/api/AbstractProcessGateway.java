/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractProcessGateway implements ProcessGateway {

	@Override
	public Object startProcess(CommandMessage cmdMsg) {
		ProcessResponse processResponse = executeProcess(cmdMsg);
		
		return (processResponse==null) ? null : processResponse.getResponse(); 
	}

}
