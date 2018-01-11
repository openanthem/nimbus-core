/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.builder;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.core.TestFrameworkIntegrationScenariosApplication;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.sample.domain.model.ui.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractStateBuilderCollectionScenariosTest {

	@Autowired QuadModelBuilder quadModelBuilder;

	protected static Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	protected QuadModel<VRSampleViewRootEntity, SampleCoreEntity> buildQuad() {
		QuadModel<VRSampleViewRootEntity, SampleCoreEntity> q = quadModelBuilder.build(createCommand());
		return q;
	}
	
	public abstract void t0_init_check();
	
	public abstract void t1_new();
	
	public abstract void t2_existing();
	
	public abstract void t3_existing_reset();
	
	public abstract void t4_existing_reset_set();
	
	public abstract void t5_existing_reset_add();
	
	public abstract void t6_existing_add();
	
	public abstract void t7_existing_addTwice();
	
	public abstract void t8_existing_addThrice();
	
	public abstract void t9_existing_addThrice_reset_addTwice();
}
