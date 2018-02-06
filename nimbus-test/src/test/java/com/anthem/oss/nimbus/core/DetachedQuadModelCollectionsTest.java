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

import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;
import com.anthem.oss.nimbus.test.sample.um.model.view.V_UMDashboard;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;

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
		Command cmd = CommandBuilder.withUri("/anthem/comm/icr/p/v_um_dashboard/_new").getCommand();
		return cmd;
	}
	
	@Before
	public void t_init() {
		QuadModel<V_UMDashboard, Object> q = quadModelBuilder.build(getCommand());
		assertNotNull(q);
		
		sessionProvider.setAttribute(getCommand(), q);
	}
	
	@Test
	public void tc01_sanity_check_core_builders() {
		QuadModel<UMCaseFlow, UMCase> q = sessionProvider.getOrThrowEx(getCommand());
		assertNotNull(q);
	}
}
