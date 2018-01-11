/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import com.antheminc.oss.nimbus.core.domain.command.Command;

/**
 * @author Soham Chakravarti
 *
 */
interface StateEventLifeCycle {

	public void onStartRuntime(ExecutionRuntime execRt);
	public void onStopRuntime(ExecutionRuntime execRt);
	
	public void onStartTxn(ExecutionTxnContext txnCtx);
	
	public void onEvent(ExecutionTxnContext txnCtx, ParamEvent event);
	
	public void onStartRootCommandExecution(Command cmd);
	
	public void onStartCommandExecution(Command cmd);

}
