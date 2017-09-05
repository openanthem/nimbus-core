package com.anthem.nimbus.platform.core.process.activiti;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import test.com.anthem.nimbus.platform.spec.model.command.TestCommandFactory;

@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoadQuadModelTest extends AbstractFrameworkIntegrationTests {
		
	@Test
	public void testLoad() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmsg.getCommand());
		Assert.assertNotNull(quadModel);
	}
	
	public static CommandMessage create_view_icr_UMCaseFlow() {
		CommandMessage msg = new CommandMessage();
		Command c = TestCommandFactory.create_view_icr_UMCaseFlow();
		msg.setCommand(c);
		return msg;
	}

}
