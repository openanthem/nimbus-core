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
package com.antheminc.oss.nimbus.domain.model.state.multitenancy;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

/**
 * @author Tony Lopez
 *
 */
@TestPropertySource(properties = { "nimbus.multitenancy.tenants.1.description=ABC",
		"nimbus.multitenancy.tenants.1.prefix=/foo/1/app", "nimbus.multitenancy.tenants.2.description=DEF",
		"nimbus.multitenancy.tenants.2.prefix=/foo/2/app", })
public class MultitenantCommandExecutionTests extends AbstractFrameworkIntegrationTests {

	@Autowired
	private CommandExecutorGateway commandGateway;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * _new on domain entity
	 */
	@Test
	public void testCreate() {
		SampleCoreEntity expected = new SampleCoreEntity();
		expected.setId(1L);
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core").setAction(Action._new).getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		SampleCoreEntity actual = ((Param<SampleCoreEntity>) response.getSingleResult()).getState();
		Assert.assertEquals(expected.getId(), actual.getId());
		Assert.assertEquals(Long.valueOf(2), actual.get_tenantId());
	}
}
