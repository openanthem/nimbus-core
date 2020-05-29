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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteError;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationError;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationResult;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 * @author Soham Chakravarti
 *
 */
@ControllerAdvice(assignableTypes=WebActionController.class)
@EnableConfigurationProperties
@ConfigurationProperties(prefix="nimbus")
@Getter
public class WebActionControllerAdvice implements ResponseBodyAdvice<Object> {
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	private static final String RESPONSE_INTERCEPTOR_BEAN_PREFIX = "httpresponsebodyinterceptor.";
	
	@Value("${nimbus.error.metricLoggingEnabled:true}")
	private boolean metricLoggingEnabled;
	
	@Getter @Setter
	private Map<Class<?>,String> exceptions;
	
	@Value("${nimbus.error.genericMsg:System Error ERR.UNIQUEID}")
	private String genericMsg;
	
	@Autowired CommandTransactionInterceptor defaultInterceptor;
	
	@Autowired BeanResolverStrategy beanResolver;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		logit.debug(()->"Processed response from "+WebActionController.class+": "
					+ "\n"+ body);
		
		MultiExecuteOutput multiOutput = defaultInterceptor.handleResponse(body);
		
		String responseBodyHeader = request.getHeaders().getFirst(Constants.HTTP_RESPONSEBODY_INTERCEPTOR_HEADER.code);
		
		if(StringUtils.isBlank(responseBodyHeader))
			return multiOutput;
		
		ResponseInterceptor<MultiExecuteOutput> interceptor = beanResolver.find(ResponseInterceptor.class, RESPONSE_INTERCEPTOR_BEAN_PREFIX+responseBodyHeader);
		
		if(interceptor == null)
			return multiOutput;
		
		interceptor.intercept(multiOutput);
		
		return multiOutput;
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public MultiExecuteOutput exception(Throwable pEx){
		ExecuteOutput<?> resp = new ExecuteOutput<>();
		ExecuteError execError = constructExecError(pEx);
		String message = constructMessage(execError);
		
		if (Optional.ofNullable(exceptions).isPresent()) {			
			if (exceptions.containsKey(pEx.getClass())) {
				message = constructMessage(exceptions.get(pEx.getClass()), execError);
			} else {
				Optional<Class<?>> hierarchyclass = exceptions.keySet()
						.stream()
						.filter(c -> null != c && c.isAssignableFrom(pEx.getClass()))
						.findFirst();
			
				if (hierarchyclass.isPresent()) 
					message = constructMessage(exceptions.get(hierarchyclass.get()), execError);
			}
		} 
		execError.setMessage(message);
		logError(execError, pEx);
		resp.setExecuteException(execError);
		return defaultInterceptor.handleResponse(resp);		
	}

	private void logError(ExecuteError execError, Throwable pEx) {
		if (FrameworkRuntimeException.class.isAssignableFrom(pEx.getClass()) && logit.getLog().isInfoEnabled()
				&& isMetricLoggingEnabled()) {
			logit.error(execError::getMessage);
		} else {
			logit.error(execError::getMessage, pEx);
		}
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
		
		return defaultInterceptor.handleResponse(resp);
	}
	
	private String constructMessage(ExecuteError err) {
		return Optional.ofNullable(genericMsg)
				.map((str) -> StringUtils.replace(str, Constants.KEY_ERR_UNIQUEID.code, err.getUniqueId()))
				.orElse(err.getMessage());
	}
	
	private String constructMessage(String configMsg, ExecuteError err) {
		return Optional.ofNullable(configMsg)
				.map((str) -> StringUtils.replace(str, Constants.KEY_ERR_UNIQUEID.code, err.getUniqueId()))
				.orElse(constructMessage(err));
	}
	
	private ExecuteError constructExecError(Throwable pEx) {
		ExecuteError execError = new ExecuteError();
		if ((FrameworkRuntimeException.class).isAssignableFrom(pEx.getClass())) {
			execError = ((FrameworkRuntimeException) pEx).getExecuteError();	
		} else 
			execError = new ExecuteError(pEx.getClass(), pEx.getMessage());
		
		return execError;
	}
	
	
}
