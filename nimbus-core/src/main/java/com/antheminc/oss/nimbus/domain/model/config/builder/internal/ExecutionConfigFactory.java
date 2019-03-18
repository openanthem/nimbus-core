/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import java.lang.reflect.AnnotatedElement;

import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.defn.validaton.ConfigPlaceholderValidator;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig.Context;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultExecutionConfig;

/**
 * @author Rakesh Patel
 *
 */
public class ExecutionConfigFactory {
	
	private final ConfigPlaceholderValidator placeholderValidator;
	
	public ExecutionConfigFactory(ReservedKeywordRegistry reservedKeywordRegistry) {
		this.placeholderValidator = new ConfigPlaceholderValidator(reservedKeywordRegistry);
	}
	
	public ExecutionConfig build(AnnotatedElement aElem) {
		final Config arr[] = aElem.getAnnotationsByType(Config.class);
		final DetourConfig arr2[] = aElem.getAnnotationsByType(DetourConfig.class);
		
		DefaultExecutionConfig executionConfig = new DefaultExecutionConfig();
		executionConfig.addAll(arr);
		executionConfig.addAll(arr2);
		executionConfig.sort();
		
		Context context = new Context();
		final ConfigPlaceholder[] placeholders = aElem.getAnnotationsByType(ConfigPlaceholder.class);
		for(ConfigPlaceholder placeholder : placeholders) {
			placeholderValidator.validate(placeholder);
			context.addPlaceholder(placeholder);
		}
		executionConfig.setContext(context);
		
		return executionConfig;
		
	}
}
