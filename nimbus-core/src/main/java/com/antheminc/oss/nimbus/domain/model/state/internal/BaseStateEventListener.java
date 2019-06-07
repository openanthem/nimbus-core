/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateEventListener;
import com.antheminc.oss.nimbus.support.JustLogit;

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
