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
package com.antheminc.oss.nimbus.domain.model.state.builder;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.SimpleCase;
import com.antheminc.oss.nimbus.test.scenarios.s3.view.VRSimpleCaseFlow;
import com.antheminc.oss.nimbus.test.scenarios.s3.view.V_SimpleDashboard;

/**
 * @author Soham Chakravarti
 *
 */
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DetachedQuadModelCollectionsTest extends AbstractFrameworkIntegrationTests {

	@Autowired QuadModelBuilder quadModelBuilder;
	@Autowired SessionProvider sessionProvider;
	
	private static Command getCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/compressopm/thebox/p/v_simpledashboard/_new").getCommand();
		return cmd;
	}
	
	@Before
	public void t_init() {
		QuadModel<V_SimpleDashboard, Object> q = quadModelBuilder.build(getCommand());
		assertNotNull(q);
		
		sessionProvider.setAttribute(getCommand(), q);
	}
	
	@Test
	public void tc01_sanity_check_core_builders() {
		QuadModel<VRSimpleCaseFlow, SimpleCase> q = sessionProvider.getAttribute(getCommand().getRootDomainUri());
		assertNotNull(q);
	}
}
