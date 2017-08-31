package com.anthem.oss.nimbus.core.web;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;

@RunWith(MockitoJUnitRunner.class)
public class WebCommandDispatcherTest {

	@InjectMocks
	private WebActionController testee;
	
	@Mock
	private WebCommandDispatcher dispatcher;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleGet() {
		final HttpServletRequest req = new MockHttpServletRequest();
		String json = "{}";
		
		Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, RequestMethod.GET, null, json)).thenReturn(obj);
		
		final Object response = this.testee.handleGet(req, json);
		
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, RequestMethod.GET, null, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandlePost() {
		final HttpServletRequest req = new MockHttpServletRequest();
		String json = "{}";
		
		Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, RequestMethod.GET, null, json)).thenReturn(obj);
		
		final Object response = this.testee.handleGet(req, json);
		
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, RequestMethod.GET, null, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
}
