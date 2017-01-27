package com.anthem.nimbus.platform.utils.exception;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.anthem.nimbus.platform.spec.model.command.ExecuteError;
import com.anthem.nimbus.platform.spec.model.command.ExecuteOutput;
import com.anthem.nimbus.platform.spec.model.command.ValidationError;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.command.ValidationResult;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice (annotations = RestController.class)
@Slf4j
public class PlatformExceptionHandlerAdvice implements ResponseBodyAdvice<Object>{

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(ValidationException e){	
		if(log.isTraceEnabled())
			log.trace("ValidationException handling in ResponseControllerAdvice");
		ExecuteOutput<?> r = new ExecuteOutput<>();
		r.setValidationResult(e.getValidationResult());
		return r;
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(MethodArgumentNotValidException e){	
		if(log.isTraceEnabled())
			log.trace("MethodArgumentNotValidException handling in ResponseControllerAdvice");
		ExecuteOutput<?> r = new ExecuteOutput<>();		
		ValidationResult v = new ValidationResult();
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		if(e.getBindingResult() != null && e.getBindingResult().getAllErrors() != null 
				&& e.getBindingResult().getAllErrors().size() > 0){
			if(log.isInfoEnabled())
				log.info("Binding result Errors:"+e.getBindingResult().getErrorCount());
			for(ObjectError objErr : e.getBindingResult().getAllErrors()){
				if(log.isTraceEnabled())
					log.trace("Populating Validation Errors");
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
	@ExceptionHandler(PlatformRuntimeException.class)
	@ResponseBody
	public ExecuteOutput<?> exception(PlatformRuntimeException p){
		if(log.isTraceEnabled())
			log.trace("PlatformRuntimeException handling in ResponseControllerAdvice");
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		log.debug("exception message"+p.getMessage());
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
		if(log.isTraceEnabled())
			log.trace("Enter beforeBodyWrite method of ResponseControllerAdvice");

		
		if(body instanceof ExecuteOutput){
			if(log.isTraceEnabled())
				log.trace("ResponseBody of type ExecuteResponse");
			return body;
		}
		ExecuteOutput<Object> r = new ExecuteOutput<Object>();
		log.debug("body object:"+body);
		r.setResult(body);
		if(log.isTraceEnabled())
			log.trace("Setting response body object in ExecuteResponse result variable");
		return r;
	}

}
