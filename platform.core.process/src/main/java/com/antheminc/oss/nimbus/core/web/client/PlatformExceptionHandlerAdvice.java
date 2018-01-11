package com.antheminc.oss.nimbus.core.web.client;

import java.util.ArrayList;
import java.util.List;

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

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecuteError;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationError;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationException;
import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationResult;
import com.antheminc.oss.nimbus.core.util.JustLogit;

@ControllerAdvice ({"com.antheminc.oss.nimbus.core.web"})
public class PlatformExceptionHandlerAdvice implements ResponseBodyAdvice<Object>{

	private JustLogit logit = new JustLogit(this.getClass());
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(ValidationException e){	
		
		logit.trace(()->"ValidationException handling in ResponseControllerAdvice");
		ExecuteOutput<?> r = new ExecuteOutput<>();
		r.setValidationResult(e.getValidationResult());
		return r;
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(MethodArgumentNotValidException e){	
		logit.trace(()->"MethodArgumentNotValidException handling in ResponseControllerAdvice");
		ExecuteOutput<?> r = new ExecuteOutput<>();		
		ValidationResult v = new ValidationResult();
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		if(e.getBindingResult() != null && e.getBindingResult().getAllErrors() != null 
				&& e.getBindingResult().getAllErrors().size() > 0){
			logit.info(()->"Binding result Errors:"+e.getBindingResult().getErrorCount());
			for(ObjectError objErr : e.getBindingResult().getAllErrors()){
				logit.trace(()->"Populating Validation Errors");
				ValidationError err = new ValidationError(){};
				err.setCode(objErr.getCode());
				err.setMsg(objErr.getDefaultMessage());
				err.setModelAlias(objErr.getObjectName());
				errors.add(err);
			}
		}			
		v.setErrors(errors);	
		r.setValidationResult(v);
		return r;		
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FrameworkRuntimeException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(FrameworkRuntimeException p){
		logit.trace(()->"FrameworkRuntimeException handling in ResponseControllerAdvice");
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		logit.debug(()->"exception message"+p.getMessage());
		ExecuteError err = p.getExecuteError();
		err.setMessage(p.getCause().getMessage());
		resp.setExecuteException(err);
		return resp;		
	}
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		logit.trace(()->"Enter beforeBodyWrite method of ResponseControllerAdvice");

		
		if(body instanceof ExecuteOutput){
			logit.trace(()->"ResponseBody of type ExecuteResponse");
			return body;
		}
		ExecuteOutput<Object> r = new ExecuteOutput<Object>();
		logit.debug(()->"body object:"+body);
		r.setResult(body);
		logit.trace(()->"Setting response body object in ExecuteResponse result variable");
		return r;
	}

}
