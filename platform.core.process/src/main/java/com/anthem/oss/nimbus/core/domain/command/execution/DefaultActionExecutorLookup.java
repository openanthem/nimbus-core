/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._lookup$execute")
public class DefaultActionExecutorLookup extends AbstractProcessTaskExecutor {

	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		throw new UnsupportedOperationException("This is supported by ParamCodeValueProvider bean. Had to define a default bean since framework mandates it. it is a BUG and need to be fixed");
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
