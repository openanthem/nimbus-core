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
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public class GridStateLoadHandler extends AbstractConfigEventHandler implements OnStateLoadHandler<Grid> {

	private final LabelStateEventHandler labelStateLoadHandler;

	public void onStateLoad(Grid configuredAnnotation, Param<?> param) {
		// set the labels retrieved from collection elements
		Map<String, Set<LabelState>> elemLabels = new HashMap<>();
		ParamConfig<?> p = param.getConfig().getType().findIfCollection().getElementConfig();

		if (!p.isLeaf()) {
			for (ParamConfig<?> colElemParamConfig : p.getType().findIfNested().getModelConfig().getParamConfigs()) {
				if (CollectionUtils.isNotEmpty(colElemParamConfig.getLabels())) {
					Set<LabelState> listParamLabels = new HashSet<>();
					colElemParamConfig.getLabels()
							.forEach((label) -> listParamLabels.add(getLabelStateLoadHandler().convert(label, param)));
					elemLabels.put(colElemParamConfig.getId(), listParamLabels);
				}
			}
		}

		param.findIfCollection().setElemLabels(elemLabels);
	}

}
