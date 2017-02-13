/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.CommandElement.Type;

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
