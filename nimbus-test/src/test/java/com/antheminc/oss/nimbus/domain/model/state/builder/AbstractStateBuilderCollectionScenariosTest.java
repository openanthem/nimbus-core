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

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.test.FrameworkIntegrationTestScenariosApplication;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrameworkIntegrationTestScenariosApplication.class)
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
