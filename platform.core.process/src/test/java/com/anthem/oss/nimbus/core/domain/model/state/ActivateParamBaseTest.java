/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.internal.BaseStateEventListener;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class ActivateParamBaseTest {

	@Autowired QuadModelBuilder quadModelBuilder;
	
	protected Command _cmd;
	
	protected QuadModel<?, SampleCoreEntity> _q;
	
	protected List<ParamEvent> _paramEvents;
	
	protected BaseStateEventListener _stateEventListener;
	
	protected static Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_core/_new").getCommand();
		return cmd;
	}
	
	protected abstract String getSourceParamPath();

	protected abstract String getTargetParamPath();
	
	@Before
	public void before() {
		_cmd = createCommand();
		_q = quadModelBuilder.build(_cmd);
		assertNotNull(_q);
		
		assertNotNull(_q.getRoot().findParamByPath(getSourceParamPath()));
		assertNotNull(_q.getRoot().findParamByPath(getTargetParamPath()));
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
	}
	
	@After
	public void after() {
		_q.getRoot().getExecutionRuntime().onStopCommandExecution(_cmd);
		_q.getRoot().getExecutionRuntime().getEventDelegator().removeTxnScopedListener(_stateEventListener);
	}
	
	protected void addListener() {
		_stateEventListener = new BaseStateEventListener() {
			@Override
			public void onStopTxn(ExecutionTxnContext txnCtx, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
				assertEquals(1, aggregatedEvents.size());
				_paramEvents = aggregatedEvents.values().iterator().next();
			}
		};
		
		_q.getRoot().getExecutionRuntime().getEventDelegator().addTxnScopedListener(_stateEventListener);
	}

}
