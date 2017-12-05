/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.StateEventListener;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.core.util.JustLogit;

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
	
	@Override
	public void onStartRootCommandExecution(Command cmd) {}
	
	@Override
	public void onStopRootCommandExecution(Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {}
	
	@Override
	public void onStartCommandExecution(Command cmd) {}
	
	@Override
	public void onStopCommandExecution(Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {}
}
