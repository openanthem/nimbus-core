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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedTransientParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.StyleState;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;

/**
 * @author Tony Lopez
 *
 */
public class ParamStateHolderTest {
	
	@SuppressWarnings("unchecked")
	private <T> Param<T> buildMock(T state) {
		Param<T> p = Mockito.mock(Param.class);
		Mockito.when(p.getLeafState()).thenReturn(state);
		return p;
	}
	
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
		String state = "hello";
		ParamStateHolder<String> testee = new ParamStateHolder<>(buildMock(state));
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
		Assert.assertTrue(isUnallowed(() -> testee.getEventSubscribers()));
		Assert.assertTrue(isUnallowed(() -> testee.getValueAccessor()));
		Assert.assertTrue(isUnallowed(() -> { testee.onStateLoadEvent(); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.onStateChangeEvent(null, null); return null; }));
		Assert.assertTrue(isUnallowed(() -> { testee.registerConsumer(null); return null; }));	
	}
	
	@Test
	public void testGetters() {
		Param<Object> ref = Mockito.mock(Param.class);
		ParamStateHolder<Object> testee = new ParamStateHolder<>(ref);
		
		Object expectedLeafState = new Object();
		Mockito.when(ref.getLeafState()).thenReturn(expectedLeafState);
		Assert.assertEquals(expectedLeafState, testee.getLeafState());
		
		Mockito.when(ref.isCollection()).thenReturn(true);
		Assert.assertTrue(testee.isCollection());
		
		Mockito.when(ref.isCollectionElem()).thenReturn(true);
		Assert.assertTrue(testee.isCollectionElem());
		
		Mockito.when(ref.isLeaf()).thenReturn(true);
		Assert.assertTrue(testee.isLeaf());
		
		Mockito.when(ref.isLeafOrCollectionWithLeafElems()).thenReturn(true);
		Assert.assertTrue(testee.isLeafOrCollectionWithLeafElems());
		
		Mockito.when(ref.isLinked()).thenReturn(true);
		Assert.assertTrue(testee.isLinked());
		
		Mockito.when(ref.isNested()).thenReturn(true);
		Assert.assertTrue(testee.isNested());
		
		Mockito.when(ref.isTransient()).thenReturn(true);
		Assert.assertTrue(testee.isTransient());
		
		Mockito.when(ref.hasContextStateChanged()).thenReturn(true);
		Assert.assertTrue(testee.hasContextStateChanged());
		
		Mockito.when(ref.isActive()).thenReturn(true);
		Assert.assertTrue(testee.isActive());
		
		Class<? extends ValidationGroup>[] expectedActiveValidationGroups = new Class[] { ValidateConditional.GROUP_0.class, ValidateConditional.GROUP_1.class };
		Mockito.when(ref.getActiveValidationGroups()).thenReturn(expectedActiveValidationGroups);
		Assert.assertArrayEquals(expectedActiveValidationGroups, testee.getActiveValidationGroups());
		
		Set<Message> expectedMessages = new HashSet<>();
		Mockito.when(ref.getMessages()).thenReturn(expectedMessages);
		Assert.assertEquals(expectedMessages, testee.getMessages());
		
		List<ParamValue> expectedValues = new ArrayList<>();
		Mockito.when(ref.getValues()).thenReturn(expectedValues);
		Assert.assertEquals(expectedValues, testee.getValues());
		
		Mockito.when(ref.isEnabled()).thenReturn(true);
		Assert.assertTrue(testee.isEnabled());
		
		Mockito.when(ref.isVisible()).thenReturn(true);
		Assert.assertTrue(testee.isVisible());
		
		Set<LabelState> expectedLabels = new HashSet<>();
		Mockito.when(ref.getLabels()).thenReturn(expectedLabels);
		Assert.assertEquals(expectedLabels, testee.getLabels());
		
		LabelState expectedDefaultLabelState = new LabelState();
		Mockito.when(ref.getDefaultLabel()).thenReturn(expectedDefaultLabelState);
		Assert.assertEquals(expectedDefaultLabelState, testee.getDefaultLabel());
		
		LabelState expectedLabelState = new LabelState();
		Mockito.when(ref.getLabel("US-en")).thenReturn(expectedLabelState);
		Assert.assertEquals(expectedLabelState, testee.getLabel("US-en"));
		
		StyleState expectedStyleState = new StyleState();
		Mockito.when(ref.getStyle()).thenReturn(expectedStyleState);
		Assert.assertSame(expectedStyleState, testee.getStyle());
		
		Mockito.when(ref.isEmpty()).thenReturn(true);
		Assert.assertTrue(testee.isEmpty());
		
		Mockito.doNothing().when(ref).activate();
		testee.activate();
		Mockito.verify(ref, Mockito.times(1)).activate();
		
		Mockito.doNothing().when(ref).deactivate();
		testee.deactivate();
		Mockito.verify(ref, Mockito.times(1)).deactivate();
	}
	
	@Test
	public void testIsAssigned() {
		Param<Object> ref = Mockito.mock(Param.class);
		ParamStateHolder<Object> testee = new ParamStateHolder<>(ref);
		
		try {
			testee.isAssigned();
			Assert.fail("Exception not thrown, but was expected.");
		} catch (InvalidConfigException e) {
			
		}
		
		Mockito.when(ref.isTransient()).thenReturn(true);
		Assert.assertTrue(testee.isTransient());
		
		MappedTransientParam expectedTransientParam = Mockito.mock(MappedTransientParam.class);
		Mockito.when(ref.findIfTransient()).thenReturn(expectedTransientParam);
		Assert.assertEquals(expectedTransientParam, testee.findIfTransient());
		
		Mockito.when(expectedTransientParam.isAssinged()).thenReturn(true);
		Assert.assertTrue(testee.isAssigned());
	}
	
	@Test
	public void testFindIfMapped() {
		Param<Object> ref = Mockito.mock(Param.class);
		ParamStateHolder<Object> testee = new ParamStateHolder<>(ref);
		
		Assert.assertNull(testee.findIfMapped());
		
		MappedParam mappedParam = Mockito.mock(MappedParam.class);
		Param mapsTo = Mockito.mock(Param.class);
		Mockito.when(ref.findIfMapped()).thenReturn(mappedParam);
		Mockito.when(mappedParam.getMapsTo()).thenReturn(mapsTo);
		Assert.assertNotNull(testee.findIfMapped());
	}
	
	@Test
	public void testSetters() {
		Param<Object> ref = Mockito.mock(Param.class);
		ParamStateHolder<Object> testee = new ParamStateHolder<>(ref);
		
		String expectedState = "mickey";
		Mockito.when(ref.isStateInitialized()).thenReturn(true);
		Assert.assertTrue(isUnallowed(() -> testee.setState(expectedState)));
		
		Mockito.when(ref.isStateInitialized()).thenReturn(false);
		Mockito.when(ref.setState(expectedState)).thenReturn(Action._new);
		Assert.assertEquals(Action._new, testee.setState(expectedState));
		
		Class<? extends ValidationGroup>[] expectedActiveValidationGroups = new Class[] { ValidateConditional.GROUP_0.class, ValidateConditional.GROUP_1.class };
		Mockito.doNothing().when(ref).setActiveValidationGroups(expectedActiveValidationGroups);
		testee.setActiveValidationGroups(expectedActiveValidationGroups);
		Mockito.verify(ref).setActiveValidationGroups(expectedActiveValidationGroups);
		
		Set<Message> expectedMessages = new HashSet<>();
		Mockito.doNothing().when(ref).setMessages(expectedMessages);
		testee.setMessages(expectedMessages);
		Mockito.verify(ref).setMessages(expectedMessages);
		
		List<ParamValue> expectedValues = new ArrayList<>();
		Mockito.doNothing().when(ref).setValues(expectedValues);
		testee.setValues(expectedValues);
		Mockito.verify(ref).setValues(expectedValues);
		
		Mockito.doNothing().when(ref).setEnabled(true);
		testee.setEnabled(true);
		Mockito.verify(ref).setEnabled(true);
		
		Mockito.doNothing().when(ref).setVisible(true);
		testee.setVisible(true);
		Mockito.verify(ref).setVisible(true);
		
		Set<LabelState> expectedLabels = new HashSet<>();
		Mockito.doNothing().when(ref).setLabels(expectedLabels);
		testee.setLabels(expectedLabels);
		Mockito.verify(ref).setLabels(expectedLabels);
		
		StyleState expectedStyle = new StyleState();
		Mockito.doNothing().when(ref).setStyle(expectedStyle);
		testee.setStyle(expectedStyle);
		Mockito.verify(ref).setStyle(expectedStyle);
	}
}
