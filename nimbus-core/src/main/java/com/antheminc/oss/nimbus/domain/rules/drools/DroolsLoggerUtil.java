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
package com.antheminc.oss.nimbus.domain.rules.drools;

import org.drools.common.InternalFactHandle;
import org.drools.spi.KnowledgeHelper;

import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * DroolsLoggerUtil will provide a logging capability within drl files <br>
 * Usage in drl files : <br>
 * <pre>
 * import function com.antheminc.oss.nimbus.domain.rules.drools.DroolsLoggerUtil.*;
 * rule "Rule"
 * 	when
 * 		some condition
 * 	then
 * 		// Log a message
 * 		info(drools, "log message");
 * 
 * 		// Log a message with an exception
 * 		info(drools, "An exception occurred", new RuntimeException("Some exception message"));
 * end
 * </pre>
 * 
 * The KnowledgeHelper interface is made available to the RHS code block of drl files as a predefined variable called "drools".
 * @see <a href="https://docs.jboss.org/drools/release/5.6.0.Final/drools-expert-docs/html/ch04.html#d0e7386">Drools Docs</a>
 * 
 * @author Swetha Vemuri
 *
 */
public class DroolsLoggerUtil {

	public DroolsLoggerUtil() {};
	
	protected static JustLogit getLogger(final KnowledgeHelper drools) {		
		InternalFactHandle factHandle = (InternalFactHandle) drools.getActivation().getPropagationContext().getFactHandleOrigin();
		final String ruleFileName = factHandle.getObject().getClass().getName();
		final String ruleResource = ruleFileName+ "." +drools.getRule().getName();
		final JustLogit logit = new JustLogit(ruleResource);
	    return logit;
	}

	public static void info(final KnowledgeHelper drools,final String msg) {
		final JustLogit logit = getLogger(drools);
	    logit.info(() -> msg); 	    
	}
	
	public static void info(final KnowledgeHelper drools,final String msg, Throwable t) {
		final JustLogit logit = getLogger(drools);
	    logit.info(() -> msg, t);
	}
	
	public static void debug(final KnowledgeHelper drools,final String msg) {
		final JustLogit logit = getLogger(drools);
	    logit.debug(() -> msg); 	    
	}
	
	public static void debug(final KnowledgeHelper drools,final String msg, Throwable t) {
		final JustLogit logit = getLogger(drools);
	    logit.debug(() -> msg, t); 	    
	}
	
	public static void trace(final KnowledgeHelper drools,final String msg) {
		final JustLogit logit = getLogger(drools);
	    logit.trace(() -> msg); 	    
	}
	
	public static void trace(final KnowledgeHelper drools,final String msg, Throwable t) {
		final JustLogit logit = getLogger(drools);
	    logit.trace(() -> msg, t); 	    
	}
	
	public static void error(final KnowledgeHelper drools,final String msg) {
		final JustLogit logit = getLogger(drools);
	    logit.error(() -> msg); 	    
	}
	
	public static void error(final KnowledgeHelper drools,final String msg, Throwable t) {
		final JustLogit logit = getLogger(drools);
	    logit.error(() -> msg, t); 	    
	}

}
