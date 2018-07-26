/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;

/**
 * @author Rakesh Patel
 *
 */
public interface ExecutionConfigProvider<A extends Annotation> {
	
	public Config getMain(A configAnnotation);
	
	public Config getException(A configAnnotation);

}
