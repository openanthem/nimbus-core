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
package com.antheminc.oss.nimbus.domain.model.config.builder.internal;

import java.lang.reflect.AnnotatedElement;

import com.antheminc.oss.nimbus.domain.cmd.exec.internal.ReservedKeywordRegistry;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
import com.antheminc.oss.nimbus.domain.defn.Execution.Let;
import com.antheminc.oss.nimbus.domain.defn.validaton.ConfigVariableValidator;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultExecutionConfig;

/**
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
public class ExecutionConfigFactory {

	private final ConfigVariableValidator configVariableValidator;

	public ExecutionConfigFactory(ReservedKeywordRegistry reservedKeywordRegistry) {
		this.configVariableValidator = new ConfigVariableValidator(reservedKeywordRegistry);
	}

	public ExecutionConfig build(AnnotatedElement aElem) {
		DefaultExecutionConfig executionConfig = new DefaultExecutionConfig();
		buildConfigs(executionConfig, aElem);
		buildDetourConfigs(executionConfig, aElem);
		buildConfigVariables(executionConfig, aElem);
		executionConfig.sort();
		return executionConfig;
	}

	protected void buildConfigs(DefaultExecutionConfig exectionConfig, AnnotatedElement aElem) {
		final Config arr[] = aElem.getAnnotationsByType(Config.class);
		exectionConfig.addAll(arr);
	}

	protected void buildConfigVariables(DefaultExecutionConfig executionConfig, AnnotatedElement aElem) {
		final Let[] variables = aElem.getAnnotationsByType(Let.class);
		for (Let variable : variables) {
			configVariableValidator.validate(variable);
			executionConfig.add(variable);
		}
	}

	protected void buildDetourConfigs(DefaultExecutionConfig exectionConfig, AnnotatedElement aElem) {
		final DetourConfig arr[] = aElem.getAnnotationsByType(DetourConfig.class);
		exectionConfig.addAll(arr);
	}
}
