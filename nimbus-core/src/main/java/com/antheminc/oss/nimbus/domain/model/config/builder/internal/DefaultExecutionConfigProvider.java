/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.model.config.builder.ExecutionConfigProvider;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultExecutionConfigProvider implements ExecutionConfigProvider<Config> {

	@Override
	public Config getMain(Config configAnnotation) {
		return configAnnotation;
	}

	@Override
	public Config getException(Config configAnnotation) {
		return null;
	}

}
