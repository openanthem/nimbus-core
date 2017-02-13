/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.contract.process.ProcessTaskExecutor;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.oss.nimbus.core.domain.CommandMessage;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default.e_pre")
public class DefaultEventExecutorPre implements ProcessTaskExecutor {

	JustLogit logit = new JustLogit(getClass());
	
	
	@Override
	public <R> R doExecute(CommandMessage cmdMsg) {
		logit.info(()->"Event Exec called for "+cmdMsg.getCommand().getEvent()+" for cmd: "+cmdMsg.getCommand());
		logit.trace(()->"With rawPayload: "+cmdMsg.getRawPayload());
		return (R)Boolean.TRUE;
	}

}
