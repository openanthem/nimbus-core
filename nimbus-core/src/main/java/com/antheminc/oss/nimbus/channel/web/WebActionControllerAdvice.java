/**
 *  Copyright 2016-2018 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidArgumentException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.UniqueIdGenerationUtil;
import com.antheminc.oss.nimbus.UnsupportedScenarioException;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteError;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationError;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationResult;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Swetha Vemuri
 * @author Soham Chakravarti
 *
 */
@ControllerAdvice(assignableTypes=WebActionController.class)
public class WebActionControllerAdvice implements ResponseBodyAdvice<Object> {
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	@Value("${application.exception.genericMsg:#{null}}")
	private String genericMsg;
	
	@Value("${application.exception.frameworkRuntimeMsg:#{null}}")
	private String frameworkRuntimeMsg;
	
	@Value("${application.exception.invalidConfigMsg:#{null}}")
	private String invalidConfigMsg;
	
	@Value("${application.exception.unsupportedMsg:#{null}}")
	private String unsupportedMsg;
	
	@Value("${application.exception.invalidArgumentMsg:#{null}}")
	private String invalidArgumentMsg;
	
	@Value("${application.exception.invalidOperationMsg:#{null}}")
	private String invalidOperationMsg;
	
	@Autowired CommandTransactionInterceptor interceptor;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		logit.debug(()->"Processed response from "+WebActionController.class+": "
					+ "\n"+ body);
		
		MultiExecuteOutput multiOutput = interceptor.handleResponse(body);
		return multiOutput;
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public MultiExecuteOutput exception(Throwable pEx){
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		ExecuteError err = new ExecuteError();
		if ((FrameworkRuntimeException.class).isAssignableFrom(pEx.getClass())) {
			err = ((FrameworkRuntimeException) pEx).getExecuteError();		
				if (pEx instanceof InvalidConfigException)			
					err.setMessage(Optional.ofNullable(invalidConfigMsg).orElse(setDefaultMessage(err.getMessage())));
				else if (pEx instanceof InvalidArgumentException)			
					err.setMessage(Optional.ofNullable(invalidArgumentMsg).orElse(setDefaultMessage(err.getMessage())));
				else if (pEx instanceof InvalidOperationAttemptedException)			
					err.setMessage(Optional.ofNullable(invalidOperationMsg).orElse(setDefaultMessage(err.getMessage())));
				else if (pEx instanceof UnsupportedScenarioException)			
					err.setMessage(Optional.ofNullable(unsupportedMsg).orElse(setDefaultMessage(err.getMessage())));
				else 
					err.setMessage(Optional.ofNullable(frameworkRuntimeMsg).orElse(setDefaultMessage(err.getMessage())));
				
			logit.error(() -> ((FrameworkRuntimeException) pEx).getExecuteError().getMessage(), pEx);
		} else {	
			err = new ExecuteError(UniqueIdGenerationUtil.generateUniqueId(), pEx.getClass(), setDefaultMessage(err.getMessage()));
			logit.error(()->genericMsg, pEx);
			resp.setExecuteException(err);
		}
		resp.setExecuteException(err);
		return interceptor.handleResponse(resp);		
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public MultiExecuteOutput exception(MethodArgumentNotValidException vEx){	
		logit.error(()->"Logging backing validation exception...",vEx);
		
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
	
	private String setDefaultMessage(String msg) {
		return Optional.ofNullable(genericMsg).orElse(msg);
	}
	
}
