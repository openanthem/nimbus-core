package com.anthem.oss.nimbus.core.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WebCommandDispatcherTest {
	
	private WebCommandDispatcher testee;
	
	@Mock
	private WebCommandBuilder builder;

	@Mock
	private CommandExecutorGateway gateway;
	
	@Before
	public void init() {
		final BeanResolverStrategy beanResolver = Mockito.mock(BeanResolverStrategy.class);
		Mockito.when(beanResolver.get(WebCommandBuilder.class)).thenReturn(this.builder);
		Mockito.when(beanResolver.get(CommandExecutorGateway.class)).thenReturn(this.gateway);
		this.testee = new WebCommandDispatcher(beanResolver);
		Mockito.verify(beanResolver, Mockito.times(1)).get(WebCommandBuilder.class);
		Mockito.verify(beanResolver, Mockito.times(1)).get(CommandExecutorGateway.class);
	}
	
	@Test
	public void testConstructor() {
		Assert.assertEquals(this.builder, this.testee.getBuilder());
		Assert.assertEquals(this.gateway, this.testee.getGateway());
	}
	
	@Test
	public void testHandleViaEventPayload() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelEvent<String> event = new ModelEvent<>();
		event.setPayload("{}");
		
		final String commandUri = "/Acme/abc/def/p/home/_new&execute";
		final Command expectedCommand = CommandBuilder.withUri(commandUri).getCommand();
		final MultiOutput expected = new MultiOutput(commandUri, new ExecutionContext(expectedCommand), Action._new, Behavior.$execute);
		
		Mockito.when(this.builder.build(request, event)).thenReturn(expectedCommand);
		Mockito.when(this.gateway.execute(expectedCommand, event.getPayload())).thenReturn(expected);
		final Object actual = this.testee.handle(request, RequestMethod.GET, event);
		Mockito.verify(this.builder, Mockito.only()).build(request, event);
		Mockito.verify(this.gateway, Mockito.times(1)).execute(expectedCommand, event.getPayload());
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testHandleViaJsonAndBuildCommand() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String payload = "{}";
		
		final String commandUri = "/Acme/abc/def/p/home/_new&execute";
		final Command expectedCommand = CommandBuilder.withUri(commandUri).getCommand();
		final MultiOutput expected = new MultiOutput(commandUri, new ExecutionContext(expectedCommand), Action._new, Behavior.$execute);
		
		Mockito.when(this.builder.build(request)).thenReturn(expectedCommand);
		Mockito.when(this.gateway.execute(expectedCommand, payload)).thenReturn(expected);
		final Object actual = this.testee.handle(request, RequestMethod.GET, "version", payload);
		Mockito.verify(this.builder, Mockito.only()).build(request);
		Mockito.verify(this.gateway, Mockito.times(1)).execute(expectedCommand, payload);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testHandleViaJsonAndProvidedCommand() {
		final String payload = "{}";
		final String commandUri = "/Acme/abc/def/p/home/_new&execute";
		final Command command = CommandBuilder.withUri(commandUri).getCommand();
		final MultiOutput expected = new MultiOutput(commandUri, new ExecutionContext(command), Action._new, Behavior.$execute);
		
		Mockito.when(this.gateway.execute(command, payload)).thenReturn(expected);
		final Object actual = this.testee.handle(command, payload);
		Mockito.verify(this.gateway, Mockito.times(1)).execute(command, payload);
		
		Assert.assertEquals(expected, actual);
	}
}
