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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.support.Holder;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandTransactionInterceptorTest {

	CommandTransactionInterceptor api;
	
	@Before
	public void init() {
		SessionProvider sessionMock = Mockito.mock(SessionProvider.class);
		BeanResolverStrategy mock = Mockito.mock(BeanResolverStrategy.class);
		Mockito.when(mock.get(any())).thenReturn(sessionMock);
		api = new CommandTransactionInterceptor(mock);
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
