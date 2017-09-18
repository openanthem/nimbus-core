/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;
import java.util.Map;

import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventListener;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class BaseStateEventListener implements StateEventListener {

	protected JustLogit logit = new JustLogit(getClass());
	
	@Override
	public void onStartRuntime(ExecutionRuntime execRt) {}
	
	@Override
	public void onStopRuntime(ExecutionRuntime execRt) {}
	
	@Override
	public void onStartTxn(ExecutionTxnContext txnCtx) {}
	
	@Override
	public void onEvent(ExecutionTxnContext txnCtx, ParamEvent event) {}
	
	@Override
	public void onStopTxn(ExecutionTxnContext txnCtx, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {}
}
