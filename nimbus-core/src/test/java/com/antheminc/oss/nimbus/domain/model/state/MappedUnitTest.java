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
package com.antheminc.oss.nimbus.domain.model.state;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedParam;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder.Mapped;


/**
 * @author Tony Lopez
 *
 */
public class MappedUnitTest {
	
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
		MappedParam<?, ?> mapped = Mockito.mock(MappedParam.class);
		ParamStateHolder<?> mapsTo = Mockito.mock(ParamStateHolder.class);
		Mapped<?, ?> testee = new Mapped(mapped, mapsTo);
		
		Assert.assertTrue(isUnallowed(() -> { testee.handleNotification(null); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.requiresConversion()));
		Assert.assertTrue(isUnallowed(() -> testee.getConfig()));
		Assert.assertTrue(isUnallowed(() -> testee.getParentModel()));
		Assert.assertTrue(isUnallowed(() -> testee.getType()));
		Assert.assertTrue(isUnallowed(() -> testee.findIfCollection()));
		Assert.assertTrue(isUnallowed(() -> testee.findIfCollectionElem()));
		Assert.assertTrue(isUnallowed(() -> testee.findIfLeaf()));
		Assert.assertTrue(isUnallowed(() -> testee.findIfLinked()));
		Assert.assertTrue(isUnallowed(() -> testee.findIfNested()));
		Assert.assertTrue(isUnallowed(() -> testee.deregisterConsumer(null)));
		Assert.assertTrue(isUnallowed(() -> { testee.emitNotification(null); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.getValueAccessor()));
		Assert.assertTrue(isUnallowed(() -> testee.getEventSubscribers()));
		Assert.assertTrue(isUnallowed(() -> { testee.onStateLoadEvent(); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.onStateChangeEvent(null, null); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.registerConsumer(null); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.findModelByPath("")));
		Assert.assertTrue(isUnallowed(() -> testee.findModelByPath(new String[] { "", "" })));
		Assert.assertTrue(isUnallowed(() -> testee.getRootDomain()));
		Assert.assertTrue(isUnallowed(() -> testee.getRootExecution()));
		Assert.assertTrue(isUnallowed(() -> { testee.initSetup(); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.initState(false); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.setStateInitialized(false); return null; }));
		Assert.assertTrue(isUnallowed(() -> testee.getLockTemplate()));
		Assert.assertTrue(isUnallowed(() -> { testee.getAspectHandlers(); return null; }));
	}
	
	@Test
	public void testGetters() {
		MappedParam<?, ?> mapped = Mockito.mock(MappedParam.class);
		ParamStateHolder<?> mapsTo = Mockito.mock(ParamStateHolder.class);
		Mapped<?, ?> testee = new Mapped(mapped, mapsTo);
		Assert.assertEquals(mapsTo, testee.getMapsTo());
		Assert.assertEquals(testee, testee.findIfMapped());
	}
}
