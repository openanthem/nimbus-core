package com.antheminc.oss.nimbus.core.web;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationException;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationResult;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WebActionControllerAdviceTest {

	@InjectMocks
	private WebActionControllerAdvice testee;
	
	@Mock
	private CommandTransactionInterceptor interceptor;
	
	@Test
	public void testSupports() {
		Assert.assertTrue(this.testee.supports(null, null));
	}
	
	@Test
	public void testBeforeBodyWrite() {
		final Object body = new Object();
		final MethodParameter returnType = Mockito.mock(MethodParameter.class);
		final MediaType selectedContentType = MediaType.ALL; 
		final Class<? extends HttpMessageConverter<?>> selectedConverterType = null;
		final ServerHttpRequest req = new ServletServerHttpRequest(new MockHttpServletRequest());
		final ServerHttpResponse res = new ServletServerHttpResponse(new MockHttpServletResponse());
		
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		Mockito.when(this.interceptor.handleResponse(body)).thenReturn(expected);
		final Object actual = this.testee.beforeBodyWrite(body, returnType, selectedContentType, selectedConverterType, req, res);
		Mockito.verify(this.interceptor, Mockito.times(1)).handleResponse(body);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testFrameworkRuntimeExceptionHandler() {
		final FrameworkRuntimeException ex = new FrameworkRuntimeException();
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		Mockito.when(this.interceptor.handleResponse(Mockito.isA(ExecuteOutput.class))).thenReturn(expected);
		final MultiExecuteOutput actual = this.testee.exception(ex);
		
		final ArgumentCaptor<ExecuteOutput> respCaptor = ArgumentCaptor.forClass(ExecuteOutput.class);
		Mockito.verify(this.interceptor, Mockito.only()).handleResponse(respCaptor.capture());
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(ex.getExecuteError(), respCaptor.getValue().getExecuteException());
	}
	
	@Test
	public void testValidationExceptionHandler() {
		final ValidationResult expectedValidationResult = new ValidationResult();
		final ValidationException ex = new ValidationException(expectedValidationResult);
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		Mockito.when(this.interceptor.handleResponse(Mockito.isA(ExecuteOutput.class))).thenReturn(expected);
		final MultiExecuteOutput actual = this.testee.exception(ex);
		
		final ArgumentCaptor<ExecuteOutput> respCaptor = ArgumentCaptor.forClass(ExecuteOutput.class);
		Mockito.verify(this.interceptor, Mockito.only()).handleResponse(respCaptor.capture());
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(expectedValidationResult, respCaptor.getValue().getValidationResult());
	}
	
	@Test
	public void testMethodArgumentNotValidExceptionHandler() throws NoSuchMethodException, SecurityException {
		final MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		final MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		Mockito.when(methodParameter.getMethod()).thenReturn(this.getClass().getMethods()[0]);
		Mockito.when(this.interceptor.handleResponse(Mockito.isA(ExecuteOutput.class))).thenReturn(expected);
		final MultiExecuteOutput actual = this.testee.exception(ex);
		
		final ArgumentCaptor<ExecuteOutput> respCaptor = ArgumentCaptor.forClass(ExecuteOutput.class);
		Mockito.verify(this.interceptor, Mockito.only()).handleResponse(respCaptor.capture());
		
		Assert.assertEquals(expected, actual);
		Assert.assertNotNull(respCaptor.getValue().getValidationResult());
		Assert.assertNotNull(respCaptor.getValue().getValidationResult().getErrors());
	}
	
	@Test
	@Ignore // BindingResult is non-nullable in MethodArgumentNotValidException
	public void testMethodArgumentNotValidExceptionHandlerNoBindingResult() throws NoSuchMethodException, SecurityException {
		final MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
		final MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, null);
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		Mockito.when(methodParameter.getMethod()).thenReturn(this.getClass().getMethods()[0]);
		Mockito.when(this.interceptor.handleResponse(Mockito.isA(ExecuteOutput.class))).thenReturn(expected);
		final MultiExecuteOutput actual = this.testee.exception(ex);
		
		final ArgumentCaptor<ExecuteOutput> respCaptor = ArgumentCaptor.forClass(ExecuteOutput.class);
		Mockito.verify(this.interceptor, Mockito.only()).handleResponse(respCaptor.capture());
		
		Assert.assertEquals(expected, actual);
		Assert.assertNotNull(respCaptor.getValue().getValidationResult());
		Assert.assertNotNull(respCaptor.getValue().getValidationResult().getErrors());
	}
	
	@Test
	public void testMethodArgumentNotValidExceptionHandlerWithErrors() throws NoSuchMethodException, SecurityException {
		final MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		final MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);
		final MultiExecuteOutput expected = new MultiExecuteOutput();
		
		List<ObjectError> errors = new ArrayList<>();
		errors.add(new ObjectError("dashboard", new String[] {"1"}, null, "dashboard-error"));
		errors.add(new ObjectError("home", new String[] {"2"}, null, "home-error"));
		
		Mockito.when(bindingResult.getAllErrors()).thenReturn(errors);
		Mockito.when(methodParameter.getMethod()).thenReturn(this.getClass().getMethods()[0]);
		Mockito.when(this.interceptor.handleResponse(Mockito.isA(ExecuteOutput.class))).thenReturn(expected);
		final MultiExecuteOutput actual = this.testee.exception(ex);
		
		final ArgumentCaptor<ExecuteOutput> respCaptor = ArgumentCaptor.forClass(ExecuteOutput.class);
		Mockito.verify(bindingResult, Mockito.times(3)).getAllErrors();
		Mockito.verify(this.interceptor, Mockito.only()).handleResponse(respCaptor.capture());
		
		Assert.assertEquals(expected, actual);
		Assert.assertNotNull(respCaptor.getValue().getValidationResult());
		Assert.assertNotNull(respCaptor.getValue().getValidationResult().getErrors());
		Assert.assertEquals("dashboard", respCaptor.getValue().getValidationResult().getErrors().get(0).getModelAlias());
		Assert.assertEquals("dashboard-error", respCaptor.getValue().getValidationResult().getErrors().get(0).getMsg());
		Assert.assertEquals("1", respCaptor.getValue().getValidationResult().getErrors().get(0).getCode());
		Assert.assertEquals("home", respCaptor.getValue().getValidationResult().getErrors().get(1).getModelAlias());
		Assert.assertEquals("home-error", respCaptor.getValue().getValidationResult().getErrors().get(1).getMsg());
		Assert.assertEquals("2", respCaptor.getValue().getValidationResult().getErrors().get(1).getCode());
	}
}
