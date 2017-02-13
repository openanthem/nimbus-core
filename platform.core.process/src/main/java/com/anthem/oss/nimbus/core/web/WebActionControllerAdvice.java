/**
 * 
 */
package com.anthem.oss.nimbus.core.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationError;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Swetha Vemuri
 * @author Soham Chakravarti
 *
 */
@ControllerAdvice(assignableTypes=WebActionController.class)
@Slf4j
public class WebActionControllerAdvice implements ResponseBodyAdvice<Object> {

	@Autowired CommandTransactionInterceptor interceptor;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		if(log.isDebugEnabled()) {
			log.debug("Processed response from "+WebActionController.class+": "
					+ "\n"+ body);
		}
		
		MultiExecuteOutput multiOutput = interceptor.handleResponse(body);
		return multiOutput;
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FrameworkRuntimeException.class)
	@ResponseBody
	public MultiExecuteOutput exception(FrameworkRuntimeException pEx){
		log.error("Logging backing execute exception...", pEx);
		
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		resp.setExecuteException(pEx.getExecuteError());
		return interceptor.handleResponse(resp);		
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public MultiExecuteOutput exception(ValidationException vEx){	
		log.error("Logging backing validation exception...", vEx);
		
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		resp.setValidationResult(vEx.getValidationResult());
		return interceptor.handleResponse(resp);
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public MultiExecuteOutput exception(MethodArgumentNotValidException vEx){	
		log.error("Logging backing validation exception...", vEx);
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(vEx.getBindingResult()!=null && vEx.getBindingResult().getAllErrors()!=null){
			
			for(ObjectError objErr : vEx.getBindingResult().getAllErrors()){
				ValidationError err = new ValidationError(){};
				err.setCode(objErr.getCode());
				err.setMsg(objErr.getDefaultMessage());
				err.setModelAlias(objErr.getObjectName());
				errors.add(err);
			}
		}			
		
		ExecuteOutput<?> resp = new ExecuteOutput<>();		
		resp.setValidationResult(new ValidationResult());
		resp.getValidationResult().setErrors(errors);	
		
		return interceptor.handleResponse(resp);
	}
	
}
