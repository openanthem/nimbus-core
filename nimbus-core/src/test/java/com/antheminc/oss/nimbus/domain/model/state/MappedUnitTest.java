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

	private Mapped<?, ?> testee;
	
	private MappedParam<?, ?> mapped;
	private ParamStateHolder<?> mapsTo;
	
	@Before
	public void init() {
		this.mapped = Mockito.mock(MappedParam.class);
		this.mapsTo = Mockito.mock(ParamStateHolder.class);
		this.testee = new Mapped(this.mapped, this.mapsTo);
	}
	
	@Test
	public void testGetters() {
		Assert.assertEquals(this.mapsTo, this.testee.getMapsTo());
		Assert.assertEquals(this.testee, this.testee.findIfMapped());
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testHandleNotification() {
		this.testee.handleNotification(null);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testRequiresConversion() {
		this.testee.requiresConversion();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetConfig() {
		this.testee.getConfig();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetParentModel() {
		this.testee.getParentModel();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetType() {
		this.testee.getType();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindIfCollection() {
		this.testee.findIfCollection();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testfindIfCollectionElem() {
		this.testee.findIfCollectionElem();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindIfLeaf() {
		this.testee.findIfLeaf();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindIfLinked() {
		this.testee.findIfLinked();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindIfNested() {
		this.testee.findIfNested();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testDeregisterConsumer() {
		this.testee.deregisterConsumer(null);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testEmitNotification() {
		this.testee.emitNotification(null);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetValueAccessor() {
		this.testee.getValueAccessor();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testEventSubscribers() {
		this.testee.getEventSubscribers();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testOnStateLoadEvent() {
		this.testee.onStateLoadEvent();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testOnStateChangeEvent() {
		this.testee.onStateChangeEvent(null, null);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testRegisterConsumer() {
		this.testee.registerConsumer(null);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindModelByPath() {
		this.testee.findModelByPath("");
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testFindModelByPathArray() {
		this.testee.findModelByPath(new String[] { "", "" });
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetRootDomain() {
		this.testee.getRootDomain();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetRootExecution() {
		this.testee.getRootExecution();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testInitSetup() {
		this.testee.initSetup();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testInitState() {
		this.testee.initState(false);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testSetStateInitialized() {
		this.testee.setStateInitialized(false);
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetLockTemplate() {
		this.testee.getLockTemplate();
	}
	
	@Test(expected = InvalidOperationAttemptedException.class)
	public void testGetAspectHandlers() {
		this.testee.getAspectHandlers();
	}
}
