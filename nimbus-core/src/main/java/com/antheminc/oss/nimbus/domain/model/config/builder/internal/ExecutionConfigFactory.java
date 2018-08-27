/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import java.lang.reflect.AnnotatedElement;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultExecutionConfig;

/**
 * @author Rakesh Patel
 *
 */
public class ExecutionConfigFactory {
	
	public ExecutionConfig build(AnnotatedElement aElem) {
		final DefaultExecutionConfig executionConfig = new DefaultExecutionConfig();
		
		final Config arr[] = aElem.getAnnotationsByType(Config.class);
		final DetourConfig arr2[] = aElem.getAnnotationsByType(DetourConfig.class);
		
		executionConfig.addAll(arr);
		executionConfig.addAll(arr2);
		
		executionConfig.sort();
		
		return executionConfig;	
		
	}
}
