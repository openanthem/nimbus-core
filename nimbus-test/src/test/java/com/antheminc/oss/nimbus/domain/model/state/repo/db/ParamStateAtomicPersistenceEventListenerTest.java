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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.support.CommandUtils;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.repo.core.SampleRepoDifferentAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
@EnableAutoConfiguration
public class ParamStateAtomicPersistenceEventListenerTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	@Qualifier("default.processGateway")
	private CommandExecutorGateway commandGateway;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private MongoTemplate mongo;

	@Test
	public void testDomainRepoAliasMismatchPersistence() throws JsonProcessingException {
		final String requestUri = "app/org/p/sample_repo_diff_alias/_new";
		final String expectedCollectionName = "person_of_interest";
		SampleRepoDifferentAlias expected = new SampleRepoDifferentAlias("Oscar", "Grouch");

		Command cmd = CommandUtils.prepareCommand(requestUri);
		final String jsonPayload = this.om.writeValueAsString(expected);
		CommandMessage cmdMsg = new CommandMessage(cmd, jsonPayload);
		this.commandGateway.execute(cmdMsg);

		// ensure the persisted entity is matching the value provided for
		// @Repo.alias, not @Domain.value
		Assert.assertEquals(expected, mongo.findById(1L, SampleRepoDifferentAlias.class, expectedCollectionName));
	}
}
