/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.test.sample.domain.model.SampleCoreEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MappedDefaultParamStateTest {
	
	@Autowired QuadModelBuilder quadModelBuilder;
	
	protected QuadModel<?, SampleCoreEntity> _q;
	
	protected static Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}

	@Before
	public void before() {
		_q = quadModelBuilder.build(createCommand());
		assertNotNull(_q);
	}
	
	@Test
	public void t00_notification_evalProcess() {
		
	}
}
