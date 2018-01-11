/**
 * 
 */
package com.antheminc.nimbus.platform.core.process.api.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import com.antheminc.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.antheminc.oss.nimbus.platform.spec.model.dsl.binder.Holder;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandTransactionInterceptorTest {

	CommandTransactionInterceptor api;
	
	@Before
	public void init() {
		api = new CommandTransactionInterceptor();
	}
	
	@Test
	public void t1_multiExec_holder() {
		final String payload = "hello";
		Holder<String> h = new Holder<>();
		h.setState(payload);
		
		MultiExecuteOutput mExecOutput = api.handleResponse(h);
		assertNotNull(mExecOutput);
		assertSame(MultiExecuteOutput.class, mExecOutput.getClass());
		assertSame(ExecuteOutput.BehaviorExecute.class, mExecOutput.getResult().get(0).getClass());
		assertSame(payload, mExecOutput.getResult().get(0).getResult());
	}
	
	@Test
	public void t1_multiExec_object() {
		final String payload = "hello";
		
		MultiExecuteOutput mExecOutput = api.handleResponse(payload);
		assertNotNull(mExecOutput);
		assertSame(MultiExecuteOutput.class, mExecOutput.getClass());
		assertSame(ExecuteOutput.BehaviorExecute.class, mExecOutput.getResult().get(0).getClass());
		assertSame(payload, mExecOutput.getResult().get(0).getResult());
	}
	
	@Test
	public void t1_multiExec_ExecOutput() {
		final String payload = "hello";
		ExecuteOutput<String> e = new ExecuteOutput<>();
		e.setResult(payload);
		
		MultiExecuteOutput mExecOutput = api.handleResponse(e);
		assertNotNull(mExecOutput);
		assertSame(MultiExecuteOutput.class, mExecOutput.getClass());
		assertSame(ExecuteOutput.BehaviorExecute.class, mExecOutput.getResult().get(0).getClass());
		assertSame(payload, mExecOutput.getResult().get(0).getResult());
	}
}
