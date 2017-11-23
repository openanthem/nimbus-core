package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.MockParam;
import com.anthem.oss.nimbus.core.domain.model.state.MockParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValuesConditionalOnStateChangeEventHandlerTest {

	private ValuesConditionalOnStateChangeEventHandler testee;
	
	private BeanResolverStrategy beanResolver;
	
	private CommandExecutorGateway gateway;
	
	@Before
	public void t0_init() {
		this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
		this.gateway = Mockito.mock(CommandExecutorGateway.class);
		Mockito.when(this.beanResolver.get(CommandExecutorGateway.class)).thenReturn(this.gateway);
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
	
	@Test
	public void t1_handleResetOnChange() {
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
	public void t2_handleDontResetOnChange_noDefaultValues() {
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
	public void t3_handleDontResetOnChange_stateIsAlreadyNull() {
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
	public void t4_handleDontResetOnChange_previousStateNotFoundInNewValues() {
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
	public void t5_handleDontResetOnChange_preservePreviousState() {
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
}
