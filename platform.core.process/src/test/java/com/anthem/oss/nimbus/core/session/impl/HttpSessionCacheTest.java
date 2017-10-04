package com.anthem.oss.nimbus.core.session.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity.ExModel;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCacheTest {
	
	@InjectMocks
	private HttpSessionCache testee;
	
	private MockHttpServletRequest request;

	@Before
	public void before() {
	    MockitoAnnotations.initMocks(this);
	    this.request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(this.request));
	}
	
	@Test
	public void testPut() {
		final String id = "testId";
		final ExecutionContext eCtx = Mockito.mock(ExecutionContext.class);
		
		this.testee.put(id, eCtx);
		
		Assert.assertEquals(eCtx, RequestContextHolder.getRequestAttributes().getAttribute(id, RequestAttributes.SCOPE_SESSION));
	}
	
	@Test
	public void testGet() {
		final String id = "testId";
		final ExecutionContext eCtx = Mockito.mock(ExecutionContext.class);
		
		RequestContextHolder.getRequestAttributes().setAttribute(id, eCtx, RequestAttributes.SCOPE_SESSION);
		
		Assert.assertEquals(eCtx, this.testee.get(id));
	}
	
	@Test
	public void testRemove() {
		final String id = "testId";
		final ExecutionContext eCtx = Mockito.mock(ExecutionContext.class);
		
		RequestContextHolder.getRequestAttributes().setAttribute(id, eCtx, RequestAttributes.SCOPE_SESSION);
		Assert.assertNotNull(RequestContextHolder.getRequestAttributes().getAttribute(id, RequestAttributes.SCOPE_SESSION));
		
		Assert.assertTrue(this.testee.remove(id));
		
		Assert.assertNull(RequestContextHolder.getRequestAttributes().getAttribute(id, RequestAttributes.SCOPE_SESSION));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testClear() {
		final ExecutionContext eCtx1 = Mockito.mock(ExecutionContext.class);
		final QuadModel qm1 = Mockito.mock(QuadModel.class);
		final ExModel em1 = Mockito.mock(ExModel.class);
		final ExecutionRuntime er1 = Mockito.mock(ExecutionRuntime.class);
		
		final ExecutionContext eCtx2 = Mockito.mock(ExecutionContext.class);
		final QuadModel qm2 = Mockito.mock(QuadModel.class);
		final ExModel em2 = Mockito.mock(ExModel.class);
		final ExecutionRuntime er2 = Mockito.mock(ExecutionRuntime.class);
		
		RequestContextHolder.getRequestAttributes().setAttribute("eCtx1", eCtx1, RequestAttributes.SCOPE_SESSION);
		RequestContextHolder.getRequestAttributes().setAttribute("eCtx2", eCtx2, RequestAttributes.SCOPE_SESSION);
		Assert.assertNotNull(RequestContextHolder.getRequestAttributes().getAttribute("eCtx1", RequestAttributes.SCOPE_SESSION));
		Assert.assertNotNull(RequestContextHolder.getRequestAttributes().getAttribute("eCtx2", RequestAttributes.SCOPE_SESSION));
		
		Mockito.when(eCtx1.getQuadModel()).thenReturn(qm1);
		Mockito.when(qm1.getRoot()).thenReturn(em1);
		Mockito.when((ExecutionRuntime)em1.getExecutionRuntime()).thenReturn(er1);
		Mockito.doNothing().when(er1).stop();
		Mockito.when(eCtx1.getQuadModel()).thenReturn(qm1);
		
		Mockito.when(eCtx2.getQuadModel()).thenReturn(qm2);
		Mockito.when(qm2.getRoot()).thenReturn(em2);
		Mockito.when((ExecutionRuntime)em2.getExecutionRuntime()).thenReturn(er2);
		Mockito.doNothing().when(er2).stop();
		Mockito.when(eCtx2.getQuadModel()).thenReturn(qm2);
		
		Assert.assertTrue(this.testee.clear());
		
		Mockito.verify(eCtx1, Mockito.times(1)).getQuadModel();
		Mockito.verify(qm1, Mockito.times(1)).getRoot();
		Mockito.verify(em1, Mockito.times(1)).getExecutionRuntime();
		Mockito.verify(er1, Mockito.times(1)).stop();
		Mockito.verify(eCtx1, Mockito.times(1)).getQuadModel();
		
		Mockito.verify(eCtx2, Mockito.times(1)).getQuadModel();
		Mockito.verify(em2, Mockito.times(1)).getExecutionRuntime();
		Mockito.verify(qm2, Mockito.times(1)).getRoot();
		Mockito.verify(er2, Mockito.times(1)).stop();
		Mockito.verify(eCtx2, Mockito.times(1)).getQuadModel();
		
		Assert.assertNull(RequestContextHolder.getRequestAttributes().getAttribute("eCtx1", RequestAttributes.SCOPE_SESSION));
		Assert.assertNull(RequestContextHolder.getRequestAttributes().getAttribute("eCtx2", RequestAttributes.SCOPE_SESSION));
	}
	
	@Test
	public void testClearFailure() {
		final ExecutionContext eCtx1 = Mockito.mock(ExecutionContext.class);
		
		RequestContextHolder.getRequestAttributes().setAttribute("eCtx1", eCtx1, RequestAttributes.SCOPE_SESSION);
		Assert.assertNotNull(RequestContextHolder.getRequestAttributes().getAttribute("eCtx1", RequestAttributes.SCOPE_SESSION));
		
		Mockito.doThrow(new RuntimeException("Some error")).when(eCtx1).getQuadModel();
		
		Assert.assertFalse(this.testee.clear());
		
		Mockito.verify(eCtx1, Mockito.times(1)).getQuadModel();
		
		Assert.assertEquals(eCtx1, RequestContextHolder.getRequestAttributes().getAttribute("eCtx1", RequestAttributes.SCOPE_SESSION));
	}
}
