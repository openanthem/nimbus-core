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
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ToolTip;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * 
 * @author Vivek Kamenini
 *
 */
public class ToolTipStateEventHandler extends AbstractConfigEventHandler implements OnStateLoadHandler<ToolTip> {

	public ToolTipStateEventHandler(CommandPathVariableResolver cmdPathResolver) {
		super(cmdPathResolver);
	}

	@Override
	public void onStateLoad(ToolTip configuredAnnotation, Param<?> param) {

		List<AnnotationConfig> annotationList = param.getConfig().getUiNatures().stream()
				.filter(uiNature -> uiNature.getAnnotation().equals(configuredAnnotation)).collect(Collectors.toList());

		if (annotationList.isEmpty() || annotationList.size() > 1) {
			throw new InvalidConfigException("Expected to find one and only one config annotation for ToolTip");
		}

		annotationList.get(0).getAttributes().put("value",
				resolvePath(StringUtils.trimToNull(configuredAnnotation.value()), param));
	}

}