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

import java.util.Optional;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Sandeep Mantha
 *
 */
@Aspect
public class DefaultLoggingHandler {

	private static final JustLogit logit = new JustLogit(DefaultLoggingHandler.class);
	
	public static final String K_METHOD_ENTRY = "[I] %s";
	
	public static final String K_METHOD_EXIT = "[O] %s";
	
	public static final String K_METHOD_EXCEPTION = "[Ex] %s";
	
	public static final String K_METHOD_ARGS = "[Args] %s %s";
	public static final String K_METHOD_RESP = "[Resp] %s %s";
	
	public static String format(String key, String methodName) {
		return String.format(key, methodName);
	}

	public static String format(String key, String methodName, String args) {
		return String.format(key, methodName, args);
	}
	
	@Around("@within(com.antheminc.oss.nimbus.support.EnableLoggingInterceptor)")
	public Object logMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String methodName = nullSafeMethodGet(proceedingJoinPoint);
		try {
			logit.info(()->format(K_METHOD_ENTRY, methodName));
			Object result =  proceedingJoinPoint.proceed();
			logit.info(()->format(K_METHOD_EXIT, methodName));
			
			return result;
			
		} catch (Throwable t) {
			nullSafeLogError(proceedingJoinPoint, methodName, t);
			
			throw t;
		}
		
	}
	
	private String nullSafeMethodGet(ProceedingJoinPoint proceedingJoinPoint) {
		return Optional.ofNullable(proceedingJoinPoint)
					.map(ProceedingJoinPoint::getSignature)
					.map(Signature::toShortString)
					.orElse("method-name-not-found");
	}
	
	private static void nullSafeLogError(ProceedingJoinPoint proceedingJoinPoint, String methodName, Throwable t) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("");
			
			Object args[] = proceedingJoinPoint.getArgs();
			if(!ArrayUtils.isEmpty(args)) {
				for(int i=0; i<args.length; i++) {
					sb.append("\n arg[").append(i).append("]: ").append(args[i]);
				}
			}
			logit.error(()->format(K_METHOD_ARGS, methodName, sb.toString()));
		} catch (Exception ex) {
			logit.error(()->"Failed to log args in interceptor. Actual exception will be logged after this trace print.", ex);
		}
		logit.error(()->format(K_METHOD_EXCEPTION, methodName), t);
	}
}
