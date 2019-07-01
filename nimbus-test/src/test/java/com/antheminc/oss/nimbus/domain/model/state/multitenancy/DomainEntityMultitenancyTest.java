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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;


/**
 * @author Tony Lopez
 *
 */
@TestPropertySource(properties = { 
		"nimbus.multitenancy.tenants.1.description=ABC",
		"nimbus.multitenancy.tenants.1.prefix=/foo/1/app",
		"nimbus.multitenancy.tenants.2.description=DEF",
		"nimbus.multitenancy.tenants.2.prefix=/foo/2/app",
})
public class DomainEntityMultitenancyTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	private CommandExecutorGateway commandGateway;
	
	@Test
	public void testMongoRecordLevelExampleSearch() {
		
		List<SampleCoreEntity> inserted = new ArrayList<>();
		SampleCoreEntity sc = new SampleCoreEntity();
		sc.setId(10L);
		sc.set_tenantId(1L);
		inserted.add(insert("sample_core", sc));
		sc = new SampleCoreEntity();
		sc.setId(11L);
		sc.set_tenantId(2L);
		inserted.add(insert("sample_core", sc));
		
		SampleCoreEntity expected = new SampleCoreEntity();
		expected.setId(10L);
		Command command = CommandBuilder.withUri("/foo/1/app/p/sample_core/_search?fn=example").getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		List<SampleCoreEntity> actual = (List<SampleCoreEntity>) response.getSingleResult();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(inserted.get(0).getId(), actual.get(0).getId());
		Assert.assertEquals(Long.valueOf(1), actual.get(0).get_tenantId());
	}
	
	@Test
	public void testMongoRecordLevelQuerySearch() {
		
		List<SampleCoreEntity> inserted = new ArrayList<>();
		SampleCoreEntity sc = new SampleCoreEntity();
		sc.setId(10L);
		sc.set_tenantId(1L);
		inserted.add(insert("sample_core", sc));
		sc = new SampleCoreEntity();
		sc.setId(11L);
		sc.set_tenantId(2L);
		inserted.add(insert("sample_core", sc));
		
		SampleCoreEntity expected = new SampleCoreEntity();
		expected.setId(1L);
		Command command = CommandBuilder.withUri("/foo/1/app/p/sample_core/_search?fn=query&where=sample_core.id.eq(10)").getCommand();
		MultiOutput response = this.commandGateway.execute(command, this.asString(expected));
		List<SampleCoreEntity> actual = (List<SampleCoreEntity>) response.getSingleResult();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(inserted.get(0).getId(), actual.get(0).getId());
		Assert.assertEquals(Long.valueOf(1), actual.get(0).get_tenantId());
	}
}
