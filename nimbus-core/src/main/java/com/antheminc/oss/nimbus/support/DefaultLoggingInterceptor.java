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
 * @author Soham Chakravarti
 *
 */
@Aspect
public class DefaultLoggingInterceptor {

	private static final JustLogit logit = new JustLogit("method-metric-logger");
	
	public static final String K_SPACE = " ";
	public static final String K_MILLI_SECS = "ms";
	
	public static final String K_METHOD_ENTRY = "[I] ";
	public static final String K_METHOD_EXIT = "[O] ";
	
	public static final String K_METHOD_EXCEPTION = "[Ex] ";
	
	public static final String K_METHOD_ARGS = "[Args] ";
	public static final String K_METHOD_RESP = "[Resp] ";
	
	public static class SimpleStopWatch {
		private long startTimeMillis;
		private long totalTimeMillis;
		
		public void start() {
			this.startTimeMillis = System.currentTimeMillis();
		}
		
		public void stop() {
			this.totalTimeMillis = System.currentTimeMillis() - this.startTimeMillis;
		}
		
		public String toString() {
			return String.valueOf(totalTimeMillis);
		}
	}
	
	public static String format(String key, String methodName) {
		return new StringBuilder().append(key).append(methodName).toString();
	}
	
	public static String format(String key, String methodName, SimpleStopWatch sw) {
		return new StringBuilder().append(key).append(methodName).append(K_SPACE).append(sw.toString()).append(K_MILLI_SECS).toString();
	}

	public static String format(String key, String methodName, String args) {
		return new StringBuilder().append(key).append(methodName).append(K_SPACE).append(args).toString();
	}
	
	@Around("@within(com.antheminc.oss.nimbus.support.EnableLoggingInterceptor)")
	public Object logMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String methodName = nullSafeMethodGet(proceedingJoinPoint);
		SimpleStopWatch sw = new SimpleStopWatch();
		
		try {
			logit.info(()->format(K_METHOD_ENTRY, methodName));
			logit.debug(()->format(K_METHOD_ARGS, methodName, nullSafeArgs(proceedingJoinPoint, methodName)));
			
			sw.start();
			Object result =  proceedingJoinPoint.proceed();
			sw.stop();
			
			logit.debug(()->format(K_METHOD_RESP, methodName, nullSafeResp(result)));
			logit.info(()->format(K_METHOD_EXIT, methodName, sw));
			
			return result;
			
		} catch (Throwable t) {
			sw.stop();
			
			nullSafeLogError(proceedingJoinPoint, methodName, t, sw);
			
			throw t;
		}
		
	}
	
	private String nullSafeMethodGet(ProceedingJoinPoint proceedingJoinPoint) {
		return Optional.ofNullable(proceedingJoinPoint)
					.map(ProceedingJoinPoint::getSignature)
					.map(Signature::toShortString)
					.orElse("method-name-not-found");
	}
	
	private static void nullSafeLogError(ProceedingJoinPoint proceedingJoinPoint, String methodName, Throwable t, SimpleStopWatch sw) {
		logit.error(()->format(K_METHOD_ARGS, methodName, nullSafeArgs(proceedingJoinPoint, methodName)));
		logit.error(()->format(K_METHOD_EXCEPTION, methodName, sw), t);
	}
	
	private static String nullSafeArgs(ProceedingJoinPoint proceedingJoinPoint, String methodName) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("");
			
			Object args[] = proceedingJoinPoint.getArgs();
			if(!ArrayUtils.isEmpty(args)) {
				for(int i=0; i<args.length; i++) {
					sb.append("\n arg[").append(i).append("]: ").append(args[i]);
				}
			}
			return sb.toString();
		} catch (Exception ex) {
			logit.error(()->"Failed to log args in interceptor.", ex);
			return "exception-encountered-in-logging-args";
		}
	}
	
	private static String nullSafeResp(Object result) {
		try {
			return new StringBuilder().append("\n resp: ").append(result).toString();
		} catch (Exception ex) {
			logit.error(()->"Failed to log result in interceptor.", ex);
			return "exception-encountered-in-logging-result";
		}
	}
}
