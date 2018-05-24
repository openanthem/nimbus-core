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

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;

/**
 * @author Sandeep Mantha
 *
 */
@Aspect
public class DefaultLoggingHandler {

	protected final JustLogit logit;
	
	public DefaultLoggingHandler(BeanResolverStrategy beanResolver) {
		logit = new JustLogit(DefaultLoggingHandler.class,beanResolver);
	}
	
	
	@Around("@within(com.antheminc.oss.nimbus.support.EnableLoggingInterceptor)")
	public Object logMethods(final ProceedingJoinPoint proceedingJoinPoint) { 
		Object value = null;	
        try {
	        	LogTemplate methodEntry = createLogTemplate("i",proceedingJoinPoint.getSignature());
	    		logit.info(methodEntry);
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
        		LogTemplate errorInMethod = createLogTemplate("e",proceedingJoinPoint.getSignature());
        		errorInMethod.setArgs(Arrays.toString(proceedingJoinPoint.getArgs()));
         	logit.info(errorInMethod);
        } finally {
	        LogTemplate methodExit = createLogTemplate("o",proceedingJoinPoint.getSignature());
        		logit.info(methodExit);	
        }
	        
	    return value;
		
	}
	
	private LogTemplate createLogTemplate(String action, Signature message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(action).append("]").append(message.toString());
		LogTemplate template = new LogTemplate();
		template.setMessage(sb.toString());
		return template;
	}
	
	
}
