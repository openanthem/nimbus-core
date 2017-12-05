/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContextLoader;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.BaseStateEventListener;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractStateEventHandlerTests extends AbstractFrameworkIngerationPersistableTests {

	//@Autowired QuadModelBuilder quadModelBuilder;
	@Autowired ExecutionContextLoader executionContextLoader;
	
	protected Command _cmd;
	
	protected QuadModel<?, ? extends IdString> _q;
	
	protected List<ParamEvent> _paramEvents;
	
	protected BaseStateEventListener _stateEventListener;
	

	protected abstract Command createCommand();
		
	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		_cmd = createCommand();
		//_q = quadModelBuilder.build(_cmd);
		_q = (QuadModel<?, ? extends IdString>)executionContextLoader.load(_cmd).getQuadModel();
		assertNotNull(_q);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
	}
	
	@After
	public void after() {
		_q.getRoot().getExecutionRuntime().onStopCommandExecution(_cmd);
		_q.getRoot().getExecutionRuntime().getEventDelegator().removeTxnScopedListener(_stateEventListener);
		
		// db cleanup
		mt.getDb().dropDatabase();
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
