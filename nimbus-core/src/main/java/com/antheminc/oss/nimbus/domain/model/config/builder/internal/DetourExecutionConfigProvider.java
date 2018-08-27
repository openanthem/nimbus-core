/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.model.config.builder.ExecutionConfigProvider;

/**
 * @author Rakesh Patel
 *
 */
public class DetourExecutionConfigProvider implements ExecutionConfigProvider<DetourConfig> {

	@Override
	public Config getMain(DetourConfig configAnnotation) {
		return configAnnotation.main();
	}

	@Override
	public Config getException(DetourConfig configAnnotation) {
		return configAnnotation.onException() != null && StringUtils.isNotBlank(configAnnotation.onException().url())
				? configAnnotation.onException()
				: null;
	}

}
