/**
 * 
 */
package com.antheminc.oss.nimbus.core;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.core.session.UserEndpointSession;
import com.antheminc.oss.nimbus.test.sample.um.model.core.UMCase;
import com.antheminc.oss.nimbus.test.sample.um.model.view.UMCaseFlow;
import com.antheminc.oss.nimbus.test.sample.um.model.view.V_UMDashboard;

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
