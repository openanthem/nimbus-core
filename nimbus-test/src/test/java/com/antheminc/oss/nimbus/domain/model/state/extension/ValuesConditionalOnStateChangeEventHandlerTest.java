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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.test.domain.mock.MockParam;
import com.antheminc.oss.nimbus.test.domain.mock.MockParamConfig;

/**
 * 
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ValuesConditionalOnStateChangeEventHandlerTest {

	private ValuesConditionalOnStateChangeEventHandler testee;
	private BeanResolverStrategy beanResolver;
	private CommandExecutorGateway gateway;
	private ExpressionEvaluator expressionEvaluator;
	
	@Before
	public void t0_init() {
		this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
		this.gateway = Mockito.mock(CommandExecutorGateway.class);
		this.expressionEvaluator = Mockito.mock(ExpressionEvaluator.class);
		Mockito.when(this.beanResolver.get(CommandExecutorGateway.class)).thenReturn(this.gateway);
		Mockito.when(this.beanResolver.get(ExpressionEvaluator.class)).thenReturn(this.expressionEvaluator);
		this.testee = new ValuesConditionalOnStateChangeEventHandler(this.beanResolver);
	}
	
	public static class SAMPLE_VALUES implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("foo", "bar"));
			return values;
		}
	}
	
	public static class SAMPLE_A_VALUES implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("A1_CODE", "A1_VALUE"));
			return values;
		}
	}
	
	public static class SAMPLE_B_VALUES implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("B1_CODE", "B1_VALUE"));
			return values;
		}
	}
	
	@Test
	public void t1_useDefault_handleResetOnChange() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition[] conditions = new Condition[]{};
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		
		targetParam.setState("some value");
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(true);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t2_useDefault_handleDontResetOnChange_noDefaultValues() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition[] conditions = new Condition[]{};
		final MockParam targetParam = new MockParam();
		
		targetParam.setState("some value");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(false);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		Assert.assertNull(targetParam.getValues());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t3_useDefault_handleDontResetOnChange_stateIsAlreadyNull() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition[] conditions = new Condition[]{};
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(false);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t4_useDefault_handleDontResetOnChange_previousStateNotFoundInNewValues() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition[] conditions = new Condition[]{};
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("unknown");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(false);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t5_useDefault_handleDontResetOnChange_preservePreviousState() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition[] conditions = new Condition[]{};
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(false);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertEquals("foo", targetParam.getState());
	}
	
	@Test
	public void t6_exclusive_handleResetOnChange() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition condition1 = Mockito.mock(Condition.class);
		final Condition condition2 = Mockito.mock(Condition.class);
		final Condition[] conditions = new Condition[]{ condition1, condition2 };
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		final Values condition1Values = Mockito.mock(Values.class);
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(true);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(configuredAnnotation.exclusive()).thenReturn(true);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		Mockito.when(condition1.when()).thenReturn("condition1expr");
		Mockito.when(condition1.then()).thenReturn(condition1Values);
		Mockito.when(condition1Values.value()).thenReturn((Class) SAMPLE_A_VALUES.class); 
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_A_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t7_nonExclusive_handleResetOnChange() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final Condition condition1 = Mockito.mock(Condition.class);
		final Condition condition2 = Mockito.mock(Condition.class);
		final Condition condition3 = Mockito.mock(Condition.class);
		final Condition[] conditions = new Condition[]{ condition1, condition2, condition3 };
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		final Values condition1Values = Mockito.mock(Values.class);
		final Values condition3Values = Mockito.mock(Values.class);
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(true);
		Mockito.when(configuredAnnotation.condition()).thenReturn(conditions);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(configuredAnnotation.exclusive()).thenReturn(false);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(defaultValues.value()).thenReturn((Class) SAMPLE_VALUES.class);
		Mockito.when(condition1.when()).thenReturn("condition1expr");
		Mockito.when(condition1.then()).thenReturn(condition1Values);
		Mockito.when(condition1Values.value()).thenReturn((Class) SAMPLE_A_VALUES.class);
		Mockito.when(condition2.when()).thenReturn("condition2expr");
		Mockito.when(condition3.when()).thenReturn("condition3expr");
		Mockito.when(condition3.then()).thenReturn(condition3Values);
		Mockito.when(condition3Values.value()).thenReturn((Class) SAMPLE_B_VALUES.class);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition2expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(false);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition3expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_B_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t8_useDefault_targetParamDisabled() {
		final ValuesConditional configuredAnnotation = Mockito.mock(ValuesConditional.class);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final Condition condition1 = Mockito.mock(Condition.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		final Values defaultValues = Mockito.mock(Values.class);
		
		targetParam.setEnabled(false);
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(configuredAnnotation.condition()).thenReturn(new Condition[] { condition1 });
		Mockito.when(condition1.when()).thenReturn("condition1expr");
		Mockito.when(configuredAnnotation.resetOnChange()).thenReturn(true);
		Mockito.when(configuredAnnotation.target()).thenReturn("../targetParam");
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		
		this.testee.handle(configuredAnnotation, null, event);
		
		Mockito.verify(configuredAnnotation).resetOnChange();
		Mockito.verify(configuredAnnotation).target();
		Mockito.verify(event).getParam();
		
		Assert.assertNull(targetParam.getValues());
	}
}
