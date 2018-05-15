/**
 * 
 */
package com.antheminc.oss.nimbus.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Rakesh Patel
 *
 */
@Aspect
public class DefaultLoggingHandler {

	protected final JustLogit logit = new JustLogit(this.getClass());
	
	public DefaultLoggingHandler() {
		System.out.println("&&&&&&&======= TEST ONLY - REMOVE ========&&&&&&&");
	}
	
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping))") 
	public void restMethods(final ProceedingJoinPoint proceedingJoinPoint) { 
		logit.info(() -> "e"+proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName()); 
	}
}
