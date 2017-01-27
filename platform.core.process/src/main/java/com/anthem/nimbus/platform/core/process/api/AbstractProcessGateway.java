/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import com.anthem.nimbus.platform.spec.contract.process.ProcessGateway;
import com.anthem.nimbus.platform.spec.contract.process.ProcessResponse;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;

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
