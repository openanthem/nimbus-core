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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection.LogLevel;

/**
 * @author Soham Chakravarti
 *
 */
@Aspect
@RefreshScope
public class DefaultLoggingInterceptor {

	private static final JustLogit logit = new JustLogit("api-metric-logger");
	
	public static final String K_SPACE = " ";
	public static final String K_MILLI_SECS = "ms";
	
	public static final String K_METHOD_ENTRY = "[I] ";
	public static final String K_METHOD_EXIT = "[O] ";
	
	public static final String K_METHOD_EXCEPTION = "[Ex] ";
	
	public static final String K_METHOD_ARGS = "[Args] ";
	public static final String K_METHOD_RESP = "[Resp] ";
	
	private Set<Class<?>> proxyProcessedBeans = new HashSet<>();
	
	public static class SimpleStopWatch {
		private long startTimeMillis;
		private String totalTimeMillis;
		
		public void start() {
			this.startTimeMillis = System.currentTimeMillis();
		}
		
		public void stop() {
			this.totalTimeMillis = String.valueOf(System.currentTimeMillis() - this.startTimeMillis);
		}
		
		public String toString() {
			return totalTimeMillis;
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
	
	@Around("@within(com.antheminc.oss.nimbus.support.EnableAPIMetricCollection)")
	public Object logMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String methodName = nullSafeMethodGet(proceedingJoinPoint);
		SimpleStopWatch sw = new SimpleStopWatch();
		
		checkAndInjectProxyReferenceToTarget(proceedingJoinPoint);
		
		try {
			EnableAPIMetricCollection configuredAnnotation = nullSafeLoggingLevel(proceedingJoinPoint);
			if(logArgs(configuredAnnotation, LogLevel.info)) {
				logit.info(()->format(K_METHOD_ARGS, methodName, nullSafeArgs(proceedingJoinPoint, methodName)));
			} else {
				logit.info(()->format(K_METHOD_ENTRY, methodName));
				logit.debug(()->format(K_METHOD_ARGS, methodName, nullSafeArgs(proceedingJoinPoint, methodName)));
			}
			
			sw.start();
			Object result =  proceedingJoinPoint.proceed();
			sw.stop();
			
			if(logResp(configuredAnnotation, LogLevel.info)) {
				logit.info(()->format(K_METHOD_RESP, methodName, nullSafeResp(result)));
			} else {
				logit.debug(()->format(K_METHOD_RESP, methodName, nullSafeResp(result)));
				logit.info(()->format(K_METHOD_EXIT, methodName, sw));
			}
			
			return result;
			
		} catch (Throwable t) {
			sw.stop();
			
			nullSafeLogError(proceedingJoinPoint, methodName, t, sw);
			
			throw t;
		}
		
	}
	
	private EnableAPIMetricCollection nullSafeLoggingLevel(ProceedingJoinPoint proceedingJoinPoint) {
		return Optional.ofNullable(proceedingJoinPoint.getTarget())
					.map(Object::getClass)
					.map(ae->AnnotationUtils.findAnnotation(ae, EnableAPIMetricCollection.class))
					.orElse(_DEFAULT);
	}
	
	private static EnableAPIMetricCollection _DEFAULT = new EnableAPIMetricCollection() {
		@Override
		public Class<? extends Annotation> annotationType() {
			return EnableAPIMetricCollection.class;
		}
		@Override
		public LogLevel args() {
			return LogLevel.debug;
		}
		@Override
		public LogLevel resp() {
			return LogLevel.debug;
		}
	};
	
	private static boolean logArgs(EnableAPIMetricCollection on, LogLevel level) {
		return Optional.ofNullable(on).filter(p->p.args()==level).isPresent();
	}
	
	private static boolean logResp(EnableAPIMetricCollection on, LogLevel level) {
		return Optional.ofNullable(on).filter(p->p.resp()==level).isPresent();
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
	
	private void checkAndInjectProxyReferenceToTarget(ProceedingJoinPoint proceedingJoinPoint) {
		Object targetBean = proceedingJoinPoint.getTarget();
		Object proxyInstance = proceedingJoinPoint.getThis();
		
		Class<?> beanClass = targetBean.getClass();
		
		if(proxyProcessedBeans.contains(beanClass))
			return;
		
		injectProxyReference(targetBean, proxyInstance);
	}
	
	private void injectProxyReference(Object targetBean, Object proxyInstance) {
		Class<?> beanClass = targetBean.getClass();
		
		try {
			List<Field> fields = FieldUtils.getFieldsListWithAnnotation(beanClass, InjectSelf.class);
	       
			if(CollectionUtils.isEmpty(fields))
	    	   		return;
	       
			if(fields.size() > 1) {
	    	   		logit.error(()->"Cannot inject self proxy reference to more than one field. Found more than 1 field with @InjectSelf annotation: "+fields+" for bean class (or its superclass): "+beanClass);
	    	   		return;
			}
	            
			setFieldValue(targetBean, fields.get(0), proxyInstance);
		}
		catch(Exception e) {
			logit.error(() -> "injectCurrentInstance failed for bean: "+beanClass, e);
		}
		finally {
			proxyProcessedBeans.add(beanClass);
		}
	}
    
    private void setFieldValue(Object object, Field field, Object value) {
        boolean isAccessible = field.isAccessible();
        
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
        		logit.error(()->"Error occured while setting the bean proxy reference to the field: "+field+" of bean: "+object);
        } finally {
            field.setAccessible(isAccessible);
        }
    }
}
