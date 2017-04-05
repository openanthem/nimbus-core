/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultEventExecutorPre implements ProcessTaskExecutor {

	JustLogit logit = new JustLogit(getClass());
	
	
	@Override
	public <R> R doExecute(CommandMessage cmdMsg) {
		logit.info(()->"Event Exec called for "+cmdMsg.getCommand().getEvent()+" for cmd: "+cmdMsg.getCommand());
		logit.trace(()->"With rawPayload: "+cmdMsg.getRawPayload());
		return (R)Boolean.TRUE;
	}

}
