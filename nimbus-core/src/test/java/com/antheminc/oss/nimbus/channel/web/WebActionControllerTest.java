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
package com.antheminc.oss.nimbus.channel.web;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;

/**
 * 
 * @author Tony Lopez
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WebActionControllerTest {

	@InjectMocks
	private WebActionController testee;
	
	@Mock
	private WebCommandDispatcher dispatcher;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleGet() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final RequestMethod requestMethod = RequestMethod.GET;
		final String version = null;
		final String json = "{}";
		
		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, json)).thenReturn(obj);
		final Object response = this.testee.handleGet(req, json);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleDelete() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final RequestMethod requestMethod = RequestMethod.DELETE;
		final String version = "1.0";
		final String json = null;

		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, json)).thenReturn(obj);
		final Object response = this.testee.handleDelete(req, version);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandlePost() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final RequestMethod requestMethod = RequestMethod.POST;
		final String version = null;
		final String json = "{}";
		
		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, json)).thenReturn(obj);
		final Object response = this.testee.handlePost(req, json);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandlePut() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final RequestMethod requestMethod = RequestMethod.PUT;
		final String version = "1.0";
		final String json = "{}";
		
		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, json)).thenReturn(obj);
		final Object response = this.testee.handlePut(req, version, json);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandlePatch() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final RequestMethod requestMethod = RequestMethod.PATCH;
		final String version = "1.0";
		final String json = "{}";
		
		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, json)).thenReturn(obj);
		final Object response = this.testee.handlePatch(req, version, json);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, json);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleEventNotify() {
		final HttpServletRequest req = new MockHttpServletRequest();
		final ModelEvent<String> event = new ModelEvent<>();
		
		final Object obj = new Object();
		
		Mockito.when(this.dispatcher.handle(req, event)).thenReturn(obj);
		final Object response = this.testee.handleEventNotify(req, event);
		Mockito.verify(this.dispatcher, Mockito.times(1)).handle(req, event);
		
		Assert.assertTrue("Expected type: " + Holder.class + " but found " + response.getClass(), response.getClass().isAssignableFrom(Holder.class));
		Assert.assertEquals(obj, ((Holder<Object>) response).getState());
	}
	
	@Test
	public void testLogin() {
		final ResponseEntity<?> response = this.testee.login();
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(null, response.getBody());
	}
}
