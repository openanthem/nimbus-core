package com.anthem.oss.nimbus.core.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;

public class DefaultExpressionHelperTest {

	private DefaultExpressionHelper testee;
	
	@Mock
	private CommandMessageConverter converter;

	@Mock
	private BeanResolverStrategy beanResolver;
	
	@Mock
	private CommandExecutorGateway executorGateway;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.beanResolver.get(CommandExecutorGateway.class)).thenReturn(this.executorGateway);
		Mockito.when(this.beanResolver.get(CommandMessageConverter.class)).thenReturn(this.converter);
		this.testee = new DefaultExpressionHelper(this.beanResolver);
		Mockito.verify(this.beanResolver, Mockito.times(1)).get(CommandExecutorGateway.class);
		Mockito.verify(this.beanResolver, Mockito.times(1)).get(CommandMessageConverter.class);
	}
	
	@Test
	public void test_json() {
		final String expected = "{}";
		final String argument1 = "argument1";
		final Object[] args = new String[]{ argument1 };
		Mockito.when(this.converter.convert(argument1)).thenReturn(expected);
		Assert.assertEquals(expected, this.testee._json(null, null, args));
		Mockito.verify(this.converter).convert(argument1);
	}
	
	@Test
	public void test_array() {
		final String argument1 = "argument1";
		final Object[] expected = new String[]{ argument1 };
		Assert.assertArrayEquals(expected, this.testee._array(null, null, expected));
	}
	
	@Test
	public void test_buildUrlFull() {
		final ExecutionContext eCtx = null;
		final DelegateExecution execution = null;
		final String[] args = new String[] { "http://localhost:9090", "foo", "bar", "bing", "bong" };
		
		final String actual = this.testee._buildUrl(eCtx, execution, args);
		
		Assert.assertEquals("http://localhost:9090?foo=bar&bing=bong&", actual);
	}
	
	@Test
	public void test_buildUrlNoParams() {
		final ExecutionContext eCtx = null;
		final DelegateExecution execution = null;
		final String[] args = new String[] { "http://localhost:9090" };
		
		final String actual = this.testee._buildUrl(eCtx, execution, args);
		
		Assert.assertEquals("http://localhost:9090?", actual);
	}
	
	@Test
	public void test_concat() {
		final ExecutionContext eCtx = null;
		final DelegateExecution execution = null;
		final String[] args = new String[] { "a", "b", "c" };
		
		final String actual = this.testee._concat(eCtx, execution, args);
		
		Assert.assertEquals("abc", actual);
	}
}
