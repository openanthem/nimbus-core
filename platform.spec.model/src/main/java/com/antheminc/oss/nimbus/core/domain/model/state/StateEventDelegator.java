/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import com.antheminc.oss.nimbus.core.domain.command.Command;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateEventDelegator extends StateEventLifeCycle {

	public void addTxnScopedListener(StateEventListener listener);
	public boolean removeTxnScopedListener(StateEventListener listener);
	
	public void onStopTxn(ExecutionTxnContext txnCtx);
	
	public void onStopRootCommandExecution(Command cmd, ExecutionTxnContext txnCtx);
	
	public void onStopCommandExecution(Command cmd, ExecutionTxnContext txnCtx);


}
