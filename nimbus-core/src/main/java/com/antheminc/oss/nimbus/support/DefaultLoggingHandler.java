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
package com.antheminc.oss.nimbus.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;

/**
 * @author Sandeep Mantha
 *
 */
@Aspect
public class DefaultLoggingHandler {

	protected final JustLogit logit;
	
	public DefaultLoggingHandler(BeanResolverStrategy beanResolver) {
		logit = new JustLogit(this.getClass(),beanResolver);
	}
	
	@Around("@annotation(com.antheminc.oss.nimbus.support.EnableLoggingInterceptor)")
	public Object restMethods(final ProceedingJoinPoint proceedingJoinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Object value = null;
		logit.info(()-> "i "+request.getMethod() + request.getRequestURI());
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
        	
        } finally {
        		logit.info(()->"o "+ request.getMethod() + request.getRequestURI());	
        }
	        
	    return value;
	}
	
	@Around("@annotation(com.antheminc.oss.nimbus.support.EnableLoggingInterceptor)")
	public Object logMethods(final ProceedingJoinPoint proceedingJoinPoint) { 
		Object value = null;	
        try {
	        	LogTemplate methodEntry = createLogTemplate("i"+proceedingJoinPoint.getSignature());
	    		logit.info(methodEntry);
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
        		LogTemplate errorInMethod = createLogTemplate("e"+proceedingJoinPoint.getSignature());
        		errorInMethod.setArgs(Arrays.toString(proceedingJoinPoint.getArgs()));
         	logit.info(errorInMethod);
        } finally {
	        LogTemplate methodExit = createLogTemplate("o"+proceedingJoinPoint.getSignature());
        		logit.info(methodExit);	
        }
	        
	    return value;
		
	}
	
	private LogTemplate createLogTemplate(String message) {
		LogTemplate template = new LogTemplate();
		template.setMessage(message);
		return template;
	}
	
	
}
