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
/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.s1.core.S1C_AnotherMain;
import com.antheminc.oss.nimbus.test.scenarios.s1.view.S1V_LineItem;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S1_GridTest extends AbstractFrameworkIntegrationTests {

	@Autowired QuadModelBuilder quadBuilder;
	
	AtomicInteger counter;
	QuadModel<?, ?> q;
	
	@Before
	public void before() {
		super.before();
		counter = new AtomicInteger();
		
		// simulate search response to be set to grid
		List<S1C_AnotherMain> searchResult = new ArrayList<>();
		searchResult.add(build());	//0
		searchResult.add(build());	//1
		searchResult.add(build());	//2
		
		// build quad
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT+"/s1v_main/_new").getCommand();
		q = quadBuilder.build(cmd);
		
		// set to grid
		q.getView().findParamByPath("/detachedItems/.m").setState(searchResult);
	}

	private S1C_AnotherMain build() {
		S1C_AnotherMain s = new S1C_AnotherMain();
		int count = counter.getAndIncrement();
		Long suffix = new Random().nextLong();
		s.setId(suffix);
		s.setValue1("value1_"+count);
		s.setValue2("value2_"+count);
		return s;
	}
	
	@Test
	public void t00_init_check() {
		Param<List<S1V_LineItem>> pDetachedItems = q.getView().findParamByPath("/detachedItems");
		assertNotNull(pDetachedItems);
		
		List<S1V_LineItem> viewList = pDetachedItems.getLeafState();
		assertNotNull(viewList);
		assertEquals(counter.get(), viewList.size());
	}
	
	@Test
	public void t01_detachedCol_activeConditional_view() {
		assertFalse(q.getView().findParamByPath("/detachedItems/0/vOnly1").isActive());
		assertTrue(q.getView().findParamByPath("/detachedItems/1/vOnly1").isActive());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/vOnly1").isActive());
	}
	
	@Test
	public void t02_detachedCol_enableConditional_view() {
		assertTrue(q.getView().findParamByPath("/detachedItems/0/vLink2").isEnabled());
		assertFalse(q.getView().findParamByPath("/detachedItems/1/vLink2").isEnabled());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/vLink2").isEnabled());
	}
	
	@Test
	public void t03_detachedCol_activeConditional_core() {
		assertFalse(q.getView().findParamByPath("/detachedItems/0/vOnly1/../.m/cOnly1").isActive());
		assertTrue(q.getView().findParamByPath("/detachedItems/1/vOnly1/../.m/cOnly1").isActive());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/vOnly1/../.m/cOnly1").isActive());
		
		assertFalse(q.getView().findParamByPath("/detachedItems.m/0/cOnly1").isActive());
		assertTrue(q.getView().findParamByPath("/detachedItems.m/1/cOnly1").isActive());
		assertFalse(q.getView().findParamByPath("/detachedItems.m/2/cOnly1").isActive());
		
		assertFalse(q.getView().findParamByPath("/detachedItems/0/.m/cOnly1").isActive());
		assertTrue(q.getView().findParamByPath("/detachedItems/1/.m/cOnly1").isActive());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/.m/cOnly1").isActive());
		
		assertFalse(q.getView().findParamByPath("/detachedItems/0/value1/.m/../cOnly1").isActive());
		assertTrue(q.getView().findParamByPath("/detachedItems/1/value1/.m/../cOnly1").isActive());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/value1/.m/../cOnly1").isActive());
	}
	
	@Test
	public void t02_detachedCol_enableConditional_core() {
		assertTrue(q.getView().findParamByPath("/detachedItems/0/vLink2/../.m/cLink2").isEnabled());
		assertFalse(q.getView().findParamByPath("/detachedItems/1/vLink2/../.m/cLink2").isEnabled());
		assertFalse(q.getView().findParamByPath("/detachedItems/2/vLink2/../.m/cLink2").isEnabled());
	}
}
