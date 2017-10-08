/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.internal.BaseStateEventListener;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractStateEventHandlerTests {

	@Autowired QuadModelBuilder quadModelBuilder;
	
	protected Command _cmd;
	
	protected QuadModel<?, SampleCoreEntity> _q;
	
	protected List<ParamEvent> _paramEvents;
	
	protected BaseStateEventListener _stateEventListener;
	
	@Autowired protected MongoOperations mongo;
	
	@Autowired protected MongoTemplate mt;
	
	
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_core/_new").getCommand();
		return cmd;
	}
		
	@Before
	public void before() {
		_cmd = createCommand();
		_q = quadModelBuilder.build(_cmd);
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
