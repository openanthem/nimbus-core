/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;
import com.anthem.oss.nimbus.test.sample.um.model.view.V_UMDashboard;

/**
 * @author Soham Chakravarti
 *
 */
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DetachedQuadModelCollectionsTest extends AbstractFrameworkIntegrationTests {

	@Autowired QuadModelBuilder quadModelBuilder;
	
	private static Command getCommand() {
		Command cmd = CommandBuilder.withUri("/anthem/comm/icr/p/v_um_dashboard/_new").getCommand();
		return cmd;
	}
	
	@Before
	public void t_init() {
		QuadModel<V_UMDashboard, Object> q = quadModelBuilder.build(getCommand());
		assertNotNull(q);
		
		UserEndpointSession.setAttribute(getCommand(), q);
	}
	
	@Test
	public void tc01_sanity_check_core_builders() {
		QuadModel<UMCaseFlow, UMCase> q = UserEndpointSession.getOrThrowEx(getCommand());
		assertNotNull(q);
	}
}
