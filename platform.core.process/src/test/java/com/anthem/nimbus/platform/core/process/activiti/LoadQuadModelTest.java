package com.anthem.nimbus.platform.core.process.activiti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.anthem.nimbus.platform.core.process.PlatformProcessEngineConfiguration;
import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;

@SpringApplicationConfiguration(classes = {PlatformProcessEngineConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class LoadQuadModelTest {
		
	@Test
	public void testLoad() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		QuadModel<?, ?> quadModel = PlatformSession.getOrThrowEx(cmsg.getCommand());
		Assert.notNull(quadModel);
	}
	
	public static CommandMessage create_view_icr_UMCaseFlow() {
		CommandMessage msg = new CommandMessage();
		Command c = TestCommandFactory.create_view_icr_UMCaseFlow();
		msg.setCommand(c);
		return msg;
	}

}
