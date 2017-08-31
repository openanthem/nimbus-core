package com.anthem.nimbus.platform.core.process.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;

@ContextConfiguration(classes = {BPMEngineConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class LoadQuadModelTest {
		
	@Test
	public void testLoad() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(cmsg.getCommand());
		Assert.notNull(quadModel);
	}
	
	public static CommandMessage create_view_icr_UMCaseFlow() {
		CommandMessage msg = new CommandMessage();
		Command c = TestCommandFactory.create_view_icr_UMCaseFlow();
		msg.setCommand(c);
		return msg;
	}

}
