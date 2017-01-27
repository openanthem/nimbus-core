/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;

/**
 * Added Optional using map. 
 * 
 * @author Jayant Chaudhuri
 * @author Soham Chakravarti
 */
@Component
public class ProcessNameExtractor {

	/**
	 * 
	 * @param cmdMessage
	 * @return
	 */
	public String getProcessName(CommandMessage cmdMessage){
		return cmdMessage.getCommand().getElement(Type.ProcessAlias).map(e->e.getAlias()).orElse(null);
	}
}
