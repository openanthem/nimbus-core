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
package com.antheminc.oss.nimbus.domain.model.state.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity.Level1;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.VRSampleViewRootEntity;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultListParamStateTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	QuadModelBuilder quadBuilder;
	
	@Test
	public void t1_add_validateSize() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		
		QuadModel<?, ?> q = quadBuilder.build(cmd);
		assertNotNull(q);
		
		List<SampleCoreNestedEntity> expected = new ArrayList<>();
		expected.add(new SampleCoreNestedEntity());
		expected.get(0).setNested_attr_complex_collection(new ArrayList<>());
		expected.get(0).getNested_attr_complex_collection().add(new Level1());
		expected.get(0).getNested_attr_complex_collection().get(0).setString1("test1");
		expected.get(0).getNested_attr_complex_collection().add(new Level1());
		expected.get(0).getNested_attr_complex_collection().get(1).setString1("test2");
		
		Param<VRSampleViewRootEntity> p_root = q.getView().findParamByPath("/");
		ListParam<SampleCoreNestedEntity> p_collection = p_root.findParamByPath("/attr_list_1_NestedEntity").findIfCollection();
		
		p_collection.add(expected.get(0));
		
		// Validate changes were made correctly
		assertThat(p_collection.size()).isEqualTo(1);
		assertThat(p_collection.getState().get(0).getNested_attr_complex_collection().size()).isEqualTo(2);
		assertThat(p_collection.getState().get(0).getNested_attr_complex_collection().get(0).getString1()).isEqualTo("test1");
		assertThat(p_collection.getState().get(0).getNested_attr_complex_collection().get(1).getString1()).isEqualTo("test2");
	}
}
