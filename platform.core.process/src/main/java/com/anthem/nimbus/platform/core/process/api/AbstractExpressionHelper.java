/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.anthem.nimbus.platform.spec.contract.process.ProcessGateway;
import com.anthem.nimbus.platform.spec.contract.process.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.Constants;
import com.anthem.oss.nimbus.core.domain.CommandElement.Type;

/**
 * @author Jayant.Chaudhuri
 *
 */
public class AbstractExpressionHelper {
	
	@Autowired 
	@Qualifier("default.processGateway")
	private ProcessGateway processGateway;
	
	/**
	 * 
	 * @return
	 */
	final public Object executeProcess(CommandMessage cmdMsg){
		ProcessResponse response = processGateway.executeProcess(cmdMsg);
		return response.getResponse();
	}
	
	/**
	 * 
	 * @param cmdMsg
	 * @param uri
	 * @return
	 */
	final public String getResolvedUri(CommandMessage cmdMsg, String uri){
		String platformUri = cmdMsg.getCommand().buildAliasTillType(cmdMsg.getCommand().root(), Type.PlatformMarker);
		StringBuilder resolvedUri = new StringBuilder(platformUri);
		resolvedUri.append(uri);
		return resolvedUri.toString();
	}

}
