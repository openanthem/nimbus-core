/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateEventDelegator extends StateEventLifeCycle {

	public void addTxnScopedListener(StateEventListener listener);
	public boolean removeTxnScopedListener(StateEventListener listener);
	
	public void onStopTxn(ExecutionTxnContext txnCtx);

}
