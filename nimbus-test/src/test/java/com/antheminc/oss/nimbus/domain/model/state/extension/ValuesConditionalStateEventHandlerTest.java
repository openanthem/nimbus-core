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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.cmd.Action;
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
public class ValuesConditionalStateEventHandlerTest {

	private ValuesConditionalStateEventHandler testee;
	private BeanResolverStrategy beanResolver;
	private CommandExecutorGateway gateway;
	private ExpressionEvaluator expressionEvaluator;
	private ParamValuesOnLoadHandler defaultParamValuesHandler;
	
	@Before
	public void t0_init() {
		this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
		this.gateway = Mockito.mock(CommandExecutorGateway.class);
		this.expressionEvaluator = Mockito.mock(ExpressionEvaluator.class);
		this.defaultParamValuesHandler = Mockito.mock(DefaultParamValuesHandler.class);
		Mockito.when(this.beanResolver.get(CommandExecutorGateway.class)).thenReturn(this.gateway);
		Mockito.when(this.beanResolver.get(ExpressionEvaluator.class)).thenReturn(this.expressionEvaluator);
		Mockito.when(this.beanResolver.get(ParamValuesOnLoadHandler.class, "paramValuesHandler")).thenReturn(this.defaultParamValuesHandler);
		this.testee = new ValuesConditionalStateEventHandler(this.beanResolver);
	}
	
	public static class SAMPLE_VALUES implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("foo", "foo"));
			values.add(new ParamValue("bar", "bar"));
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
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{}, true, Event.DEFAULT_ORDER_NUMBER, true, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		targetParam.setState("some value");
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		
		decoratedParam.putParam(targetParam, "../targetParam");
		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(defaultValues, decoratedParam, targetParam)).thenReturn(new SAMPLE_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t2_useDefault_handleDontResetOnChange_noDefaultValues() {
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{}, true, Event.DEFAULT_ORDER_NUMBER, false, "../targetParam");
		
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		targetParam.setState("some value");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(null, decoratedParam, targetParam)).thenReturn(null);
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		Assert.assertNull(targetParam.getValues());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t3_useDefault_handleDontResetOnChange_stateIsAlreadyNull() {
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{}, true, Event.DEFAULT_ORDER_NUMBER, false, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(defaultValues, decoratedParam, targetParam)).thenReturn(new SAMPLE_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t4_useDefault_handleDontResetOnChange_previousStateNotFoundInNewValues() {
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{}, true, Event.DEFAULT_ORDER_NUMBER, false, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("unknown");
		decoratedParam.putParam(targetParam, "../targetParam");

		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(defaultValues, decoratedParam, targetParam)).thenReturn(new SAMPLE_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t5_useDefault_handleDontResetOnChange_preservePreviousState() {
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{}, true, Event.DEFAULT_ORDER_NUMBER, false, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");

		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(defaultValues, decoratedParam, targetParam)).thenReturn(new SAMPLE_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertEquals("foo", targetParam.getState());
	}
	
	@Test
	public void t6_exclusive_handleResetOnChange() {
		final Condition condition1 = createCondition("condition1expr", createValues(SAMPLE_VALUES.class, null));
		final Condition condition2 = createCondition("condition2expr", createValues(SAMPLE_VALUES.class, null));
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{ condition1, condition2 }, true, Event.DEFAULT_ORDER_NUMBER, true, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");

		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(condition1.then(), decoratedParam, targetParam)).thenReturn(new SAMPLE_A_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_A_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t7_nonExclusive_handleResetOnChange() {
		final Condition condition1 = createCondition("condition1expr", createValues(SAMPLE_VALUES.class, null));
		final Condition condition2 = createCondition("condition2expr", createValues(SAMPLE_VALUES.class, null));
		final Condition condition3 = createCondition("condition3expr", createValues(SAMPLE_VALUES.class, null));
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{ condition1, condition2, condition3 }, false, Event.DEFAULT_ORDER_NUMBER, true, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		targetParam.setState("foo");
		decoratedParam.putParam(targetParam, "../targetParam");
		
		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition2expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(false);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition3expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(condition3.then(), decoratedParam, targetParam)).thenReturn(new SAMPLE_B_VALUES().getValues(""));
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		final List<ParamValue> expectedValues = new SAMPLE_B_VALUES().getValues("");
		Assert.assertEquals(expectedValues.size(), targetParam.getValues().size());
		Assert.assertEquals(expectedValues.get(0).getCode(), targetParam.getValues().get(0).getCode());
		Assert.assertEquals(expectedValues.get(0).getLabel(), targetParam.getValues().get(0).getLabel());
		Assert.assertNull(targetParam.getState());
	}
	
	@Test
	public void t8_useDefault_targetParamDisabled() {
		final Condition condition1 = createCondition("condition1expr", createValues(SAMPLE_VALUES.class, null));
		final ValuesConditional configuredAnnotation = createValuesConditional(new Condition[]{ condition1 }, true, Event.DEFAULT_ORDER_NUMBER, true, "../targetParam");
		final Values defaultValues = createValues(SAMPLE_VALUES.class, null);
		final ParamEvent event = Mockito.mock(ParamEvent.class);
		final MockParam decoratedParam = new MockParam();
		final MockParam targetParam = new MockParam();
		targetParam.setState("bar");
		targetParam.setEnabled(false);
		((MockParamConfig) targetParam.getConfig()).setValues(defaultValues);
		decoratedParam.putParam(targetParam, "../targetParam");
		
		List<ParamValue> expectedValues = new SAMPLE_VALUES().getValues(null);
		
		Mockito.when(event.getAction()).thenReturn(Action._update);
		Mockito.when(event.getParam()).thenReturn((Param) decoratedParam);
		Mockito.when(this.expressionEvaluator.getValue(Mockito.eq("condition1expr"), Mockito.isA(ParamStateHolder.class), Mockito.eq(Boolean.class))).thenReturn(true);
		Mockito.when(this.defaultParamValuesHandler.buildParamValues(condition1.then(), decoratedParam, targetParam)).thenReturn(expectedValues);
		
		this.testee.onStateChange(configuredAnnotation, null, event);
		
		Mockito.verify(event).getParam();
		
		// Validate values are still as expected
		Assert.assertEquals(expectedValues, targetParam.getValues());
		// Validate state is preserved
		Assert.assertEquals("bar", targetParam.getLeafState());
	}
	
	private Condition createCondition(String when, Values then) {
		return new Condition() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Condition.class;
			}

			@Override
			public Values then() {
				return then;
			}

			@Override
			public String when() {
				return when;
			}
			
		};
	}
	
	private Values createValues(Class<? extends Source> value, String url) {
		return new Values() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Values.class;
			}

			@Override
			public Class<? extends Source> value() {
				return value;
			}

			@Override
			public String url() {
				return url;
			}
			
		};
	}
	
	private ValuesConditional createValuesConditional(Condition[] condition, boolean exclusive, int order, boolean resetOnChange, String targetPath) {
		return new ValuesConditional() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return ValuesConditional.class;
			}

			@Override
			public Condition[] condition() {
				return condition;
			}

			@Override
			public boolean exclusive() {
				return exclusive;
			}

			@Override
			public int order() {
				return order;
			}

			@Override
			public boolean resetOnChange() {
				return resetOnChange;
			}

			@Override
			public String targetPath() {
				return targetPath;
			}
			
		};
	}
}
