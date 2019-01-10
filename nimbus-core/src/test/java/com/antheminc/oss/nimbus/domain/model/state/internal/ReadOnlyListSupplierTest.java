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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;

/**
 * @author Tony Lopez
 *
 */
public class ReadOnlyListSupplierTest {

	private ReadOnlyListSupplier<String> testee;
	
	@SuppressWarnings("unchecked")
	private ListParam<String> buildMock(List<String> state) {
		ListParam<String> p = Mockito.mock(ListParam.class);
//		NestedCollection<String> pType = Mockito.mock(NestedCollection.class);
//		ListModel<String> pModel = Mockito.mock(ListModel.class);
//		CollectionsTemplate<List<Param<?>>, Param<?>> pCollectionsTemplate = Mockito.mock(CollectionsTemplate.class);
//		Mockito.when(p.getType()).thenReturn(pType);
//		Mockito.when(pType.getModel()).thenReturn(pModel);
//		Mockito.when(pModel.templateParams()).thenReturn(pCollectionsTemplate);
//		Mockito.when(p.getLeafState()).thenReturn(state);
//		Mockito.when(p.size()).thenReturn(state.size());
//		Mockito.when(pCollectionsTemplate.isNullOrEmpty()).thenReturn(false);
//		Mockito.when(pCollectionsTemplate.contains()).thenReturn(false);
		return p;
	}

	@Test
	public void testUnallowedAccesses() {
		List<String> state = Arrays.asList(new String[] {"red", "green", "blue"});
		testee = new ReadOnlyListSupplier<String>(buildMock(state));
		Assert.assertTrue(isUnallowed(() -> testee.iterator()));
		Assert.assertTrue(isUnallowed(() -> testee.add(null)));
		Assert.assertTrue(isUnallowed(() -> testee.remove(null)));
		Assert.assertTrue(isUnallowed(() -> testee.containsAll(null)));
		Assert.assertTrue(isUnallowed(() -> testee.addAll(null)));
		Assert.assertTrue(isUnallowed(() -> testee.addAll(0, null)));
		Assert.assertTrue(isUnallowed(() -> testee.removeAll(null)));
		Assert.assertTrue(isUnallowed(() -> testee.retainAll(null)));
		Assert.assertTrue(isUnallowed(() -> { testee.clear(); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.set(0, null)));
		Assert.assertTrue(isUnallowed(() -> { testee.add(0, null); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.remove(0)));
		Assert.assertTrue(isUnallowed(() -> testee.indexOf(0)));
		Assert.assertTrue(isUnallowed(() -> testee.lastIndexOf(0)));
		Assert.assertTrue(isUnallowed(() -> testee.listIterator()));
		Assert.assertTrue(isUnallowed(() -> testee.listIterator(0)));
		Assert.assertTrue(isUnallowed(() -> testee.subList(0, 1)));
	}
	
	private boolean isUnallowed(Supplier<Object> s) {
		try {
			s.get();
		} catch (InvalidOperationAttemptedException e) {
			return true;
		}
		return false;
	}
}
