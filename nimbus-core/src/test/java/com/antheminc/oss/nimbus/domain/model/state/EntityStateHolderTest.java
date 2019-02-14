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
package com.antheminc.oss.nimbus.domain.model.state;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.EntityStateHolder;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;

/**
 * @author Tony Lopez
 *
 */
public class EntityStateHolderTest {

	private boolean isUnallowed(Supplier<Object> s) {
		try {
			s.get();
		} catch (InvalidOperationAttemptedException e) {
			return true;
		}
		return false;
	}
	
	@Test
	public void testUnallowedAccesses() {
		EntityState<String> ref = Mockito.mock(EntityState.class);
		EntityStateHolder<String, EntityState<String>> testee = new EntityStateHolder<>(ref);
		
		Assert.assertTrue(isUnallowed(() -> testee.getConfig()));
		Assert.assertTrue(isUnallowed(() -> testee.findModelByPath("")));
		Assert.assertTrue(isUnallowed(() -> testee.findModelByPath(new String[] { "", "" })));
		Assert.assertTrue(isUnallowed(() -> testee.getRootDomain()));
		Assert.assertTrue(isUnallowed(() -> testee.getRootExecution()));
		Assert.assertTrue(isUnallowed(() -> { testee.initSetup(); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.initState(false); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.setStateInitialized(false); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.getLockTemplate()));
		Assert.assertTrue(isUnallowed(() -> { testee.getAspectHandlers(); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.findIfMapped()));
	}
	
	@Test
	public void testGetters() {
		EntityState<String> ref = Mockito.mock(EntityState.class);
		EntityStateHolder<String, EntityState<String>> testee = new EntityStateHolder<>(ref);
		
		Mockito.when(ref.getPath()).thenReturn("A");
		Assert.assertEquals("A", testee.getPath());
		
		Mockito.when(ref.getBeanPath()).thenReturn("B");
		Assert.assertEquals("B", testee.getBeanPath());
		
		Mockito.when(ref.getConfigId()).thenReturn("C");
		Assert.assertEquals("C", testee.getConfigId());
		
		Mockito.when(ref.findStateByPath("abc")).thenReturn("D");
		Assert.assertEquals("D", testee.findStateByPath("abc"));
		
		Mockito.when(ref.findParamByPath("abc")).thenReturn(null);
		Assert.assertNull(testee.findParamByPath("abc"));
		
		String[] arr = new String[] { "1", "2" };
		Mockito.when(ref.findParamByPath(arr)).thenReturn(null);
		Assert.assertNull(testee.findParamByPath(arr));
		
		Mockito.when(ref.isMapped()).thenReturn(true);
		Assert.assertTrue(testee.isMapped());
		
		Mockito.when(ref.isRoot()).thenReturn(true);
		Assert.assertTrue(testee.isRoot());
		
		Mockito.doNothing().when(ref).fireRules();
		testee.fireRules();
		Mockito.verify(ref, Mockito.times(1)).fireRules();
		
		Mockito.when(ref.isStateInitialized()).thenReturn(false);
		Assert.assertTrue(testee.onLoad());
		
		Mockito.when(ref.isStateInitialized()).thenReturn(true);
		Assert.assertFalse(testee.onLoad());
	}
	
	@Test
	public void testFindParamByPath() {
		Param<Object> ref = Mockito.mock(Param.class);
		EntityStateHolder<Object, EntityState<Object>> testee = new EntityStateHolder<>(ref);
		
		Mockito.when(ref.findParamByPath("abc")).thenReturn(null);
		Assert.assertNull(testee.findParamByPath("abc"));
		
		Param<Object> nestedParam = Mockito.mock(Param.class);
		Mockito.when(ref.findParamByPath("abc")).thenReturn(nestedParam);
		Assert.assertNotNull(testee.findParamByPath("abc"));
		
		Mockito.when(ref.findParamByPath("abc")).thenReturn(ref);
		Assert.assertNotNull(testee.findParamByPath("abc"));
		
		Param<Object> ref2 = Mockito.mock(Param.class);
		ParamStateHolder<Object> testee2 = new ParamStateHolder<>(ref);
		Mockito.when(ref2.findParamByPath("abc")).thenReturn(ref2);
		Assert.assertNotNull(testee2.findParamByPath("abc"));
	}
}
