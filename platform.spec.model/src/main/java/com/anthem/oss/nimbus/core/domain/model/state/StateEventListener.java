/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.List;
import java.util.Map;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateEventListener extends StateEventLifeCycle {

	public void onStopTxn(ExecutionTxnContext txnCtx, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);

	public void onStopRootCommandExecution(Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);
	
	public void onStopCommandExecution(Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);

}
