/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

/**
 * @author Soham Chakravarti
 *
 */
interface StateEventLifeCycle {

	public void onStartRuntime(ExecutionRuntime execRt);
	public void onStopRuntime(ExecutionRuntime execRt);
	
	public void onStartTxn(ExecutionTxnContext txnCtx);
	
	public void onEvent(ParamEvent event);
}
