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
package com.antheminc.oss.nimbus.test.scenarios.multidb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.test.scenarios.multidb.core.SampleMultiDbDefaultEntity;
import com.antheminc.oss.nimbus.test.scenarios.multidb.core.SampleMultiDbDomainPrimary;
import com.antheminc.oss.nimbus.test.scenarios.multidb.core.SampleMultiDbDomainSecondary;

/**
 * @author Tony Lopez
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultipleMongoDBTestApplication.class)
@ActiveProfiles("test-multi-db")
public class MultipleMongoDBTest {

	public final static String PRIMARY_ALIAS = "sample_multidb_primary";
	public final static String SECONDARY_ALIAS = "sample_multidb_secondary";
	public final static String DEFAULT_ALIAS = "sample_multidb_default";

	protected QuadModel<?, SampleMultiDbDomainPrimary> _qPrimary;
	protected QuadModel<?, SampleMultiDbDomainSecondary> _qSecondary;
	protected QuadModel<?, SampleMultiDbDefaultEntity> _qDefault;

	@Autowired
	protected ExecutionContextLoader executionContextLoader;

	@Autowired
	@Qualifier("default.MongoOperationsPrimary")
	private MongoOperations mongoTemplatePrimary;

	@Autowired
	@Qualifier("default.MongoOperationsSecondary")
	private MongoOperations mongoTemplateSecondary;

	@SuppressWarnings("unchecked")
	@Before
	public void init() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/" + PRIMARY_ALIAS + "/_new").getCommand();
		_qPrimary = (QuadModel<?, SampleMultiDbDomainPrimary>) executionContextLoader.load(cmd).getQuadModel();

		cmd = CommandBuilder.withUri("/hooli/thebox/p/" + SECONDARY_ALIAS + "/_new").getCommand();
		_qSecondary = (QuadModel<?, SampleMultiDbDomainSecondary>) executionContextLoader.load(cmd).getQuadModel();

		cmd = CommandBuilder.withUri("/hooli/thebox/p/" + DEFAULT_ALIAS + "/_new").getCommand();
		_qDefault = (QuadModel<?, SampleMultiDbDefaultEntity>) executionContextLoader.load(cmd).getQuadModel();
	}

	@Test
	public void testInit() {
		assertNotNull(_qPrimary);
		assertNotNull(_qSecondary);
	}

	/**
	 * <p>Persist two different objects via set state that are configured to use
	 * two different mongo databases. The first object should be persisted into
	 * the [primary] database, and the second should be persisted to the
	 * [secondary] database with no explicit instruction other than the
	 * configuration given in the domain entity definitions:
	 * SampleMultiDbDomainPrimary, SampleMultiDbDomainSecondary
	 */
	@Test
	public void testPrimaryPersistence() {
		SampleMultiDbDomainPrimary expectedPrimary = new SampleMultiDbDomainPrimary(1L, "primary-1", "primary-2");
		_qPrimary.getRoot().findParamByPath(PRIMARY_ALIAS).setState(expectedPrimary);
		_qSecondary.getRoot().findParamByPath(SECONDARY_ALIAS)
				.setState(new SampleMultiDbDomainSecondary(1L, "secondary-1", "secondary-2"));
		assertEquals(expectedPrimary,
				this.mongoTemplatePrimary.findById(1L, SampleMultiDbDomainPrimary.class, PRIMARY_ALIAS));
		assertNull(this.mongoTemplatePrimary.findById(1L, SampleMultiDbDomainSecondary.class, SECONDARY_ALIAS));
	}

	/**
	 * <p>Persist two different objects via set state that are configured to use
	 * two different mongo databases. The first object should be persisted into
	 * the [secondary] database, and the second should be persisted to the
	 * [primary] database with no explicit instruction other than the
	 * configuration given in the domain entity definitions:
	 * SampleMultiDbDomainPrimary, SampleMultiDbDomainSecondary
	 */
	@Test
	public void testSecondaryPersistance() {
		SampleMultiDbDomainSecondary expectedSecondary = new SampleMultiDbDomainSecondary(1L, "secondary-1", "secondary-2");
		_qSecondary.getRoot().findParamByPath(SECONDARY_ALIAS).setState(expectedSecondary);
		_qPrimary.getRoot().findParamByPath(PRIMARY_ALIAS)
				.setState(new SampleMultiDbDomainPrimary(1L, "primary-1", "primary-2"));
		assertEquals(expectedSecondary,
				this.mongoTemplateSecondary.findById(1L, SampleMultiDbDomainSecondary.class, SECONDARY_ALIAS));
		assertNull(this.mongoTemplateSecondary.findById(1L, SampleMultiDbDomainPrimary.class, PRIMARY_ALIAS));
	}

	/**
	 * <p>Ensure regular persistance with something like rep_mongodb still works
	 * alongside rep_custom implementations.
	 */
	@Test
	public void testDefaultPersistance() {
		SampleMultiDbDefaultEntity expected = new SampleMultiDbDefaultEntity(1L, "a", "b");
		_qDefault.getRoot().findParamByPath(DEFAULT_ALIAS).setState(expected);
		assertEquals(expected, this.mongoTemplatePrimary.findById(1L, SampleMultiDbDefaultEntity.class, DEFAULT_ALIAS));
		assertNull(this.mongoTemplateSecondary.findById(1L, SampleMultiDbDefaultEntity.class, DEFAULT_ALIAS));
	}
}
