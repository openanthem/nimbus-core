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

	/**
	 * _delete on domain entity
	 */
	@Test
	public void testDelete() {
		SampleCoreEntity expected = insertSingleSampleCore(1L, 2L);
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._delete).getCommand();
		MultiOutput response = this.commandGateway.execute(command, null);
		Assert.assertTrue((Boolean) response.getSingleResult());
		Assert.assertFalse(exists(1L, SampleCoreEntity.class, "sample_core"));
	}

	/**
	 * _delete on domain entity having a different tenant id than what is
	 * provided in the command url
	 */
	@Test
	public void testDeleteUnallowed() {
		SampleCoreEntity expected = insertSingleSampleCore(2L, 1L);
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._delete).getCommand();
		thrown.expect(FrameworkRuntimeException.class);
		thrown.expectMessage("Entity not found for /foo/2/app/p/sample_core:" + expected.getId());
		this.commandGateway.execute(command, null);
	}

	/**
	 * _get on domain entity
	 */
	@Test
	public void testRead() {
		SampleCoreEntity expected = insertSingleSampleCore(3L, 2L);
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._get).getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		SampleCoreEntity actual = ((Param<SampleCoreEntity>) response.getSingleResult()).getState();
		Assert.assertEquals(expected.getId(), actual.getId());
		Assert.assertEquals(expected.get_tenantId(), actual.get_tenantId());
	}

	/**
	 * _get on domain entity having a different tenant id than what is provided
	 * in the command url
	 */
	@Test
	public void testReadUnallowed() {
		SampleCoreEntity expected = insertSingleSampleCore(4L, 1L);
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._get).getCommand();
		thrown.expect(FrameworkRuntimeException.class);
		thrown.expectMessage("Entity not found for /foo/2/app/p/sample_core:" + expected.getId());
		this.commandGateway.execute(command, null);
	}

	/**
	 * _replace on domain entity
	 */
	@Test
	public void testReplace() {
		SampleCoreEntity expected = insertSingleSampleCore(5L, 2L);
		expected.setAttr_String("foo");
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._replace).getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		Assert.assertTrue((Boolean) response.getSingleResult());
		SampleCoreEntity actual = this.find(expected.getId(), SampleCoreEntity.class, "sample_core");
		Assert.assertEquals(expected.getId(), actual.getId());
		Assert.assertEquals(expected.get_tenantId(), actual.get_tenantId());
		Assert.assertEquals(expected.getAttr_String(), actual.getAttr_String());
	}

	/**
	 * _replace on domain entity having a different tenant id than what is
	 * provided in the command url
	 */
	@Test
	public void testReplaceUnallowed() {
		SampleCoreEntity expected = insertSingleSampleCore(6L, 1L);
		expected.setAttr_String("foo");
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._replace).getCommand();
		thrown.expect(FrameworkRuntimeException.class);
		thrown.expectMessage("Entity not found for /foo/2/app/p/sample_core:" + expected.getId());
		this.commandGateway.execute(command, this.asString(expected));
	}
	
	/**
	 * _update on domain entity
	 */
	@Test
	public void testUpdate() {
		SampleCoreEntity expected = insertSingleSampleCore(7L, 2L);
		expected.setAttr_String("foo");
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._update).getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		Assert.assertTrue((Boolean) response.getSingleResult());
		SampleCoreEntity actual = this.find(expected.getId(), SampleCoreEntity.class, "sample_core");
		Assert.assertEquals(expected.getId(), actual.getId());
		Assert.assertEquals(expected.get_tenantId(), actual.get_tenantId());
		Assert.assertEquals(expected.getAttr_String(), actual.getAttr_String());
	}

	/**
	 * _update on domain entity having a different tenant id than what is
	 * provided in the command url
	 */
	@Test
	public void testUpdateUnallowed() {
		SampleCoreEntity expected = insertSingleSampleCore(8L, 1L);
		expected.setAttr_String("foo");
		Command command = CommandBuilder.withUri("/foo/2/app/p/sample_core:" + expected.getId()).setAction(Action._update).getCommand();
		thrown.expect(FrameworkRuntimeException.class);
		thrown.expectMessage("Entity not found for /foo/2/app/p/sample_core:" + expected.getId());
		this.commandGateway.execute(command, this.asString(expected));
	}

	private SampleCoreEntity insertSingleSampleCore(Long id, Long tenantId) {
		SampleCoreEntity expected = new SampleCoreEntity();
		expected.setId(id);
		expected.set_tenantId(tenantId);
		return insert("sample_core", expected);
	}
}
