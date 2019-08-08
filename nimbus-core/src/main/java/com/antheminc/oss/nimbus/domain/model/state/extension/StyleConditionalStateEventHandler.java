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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.lang.annotation.Annotation;
import java.util.Set;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.Style;
import com.antheminc.oss.nimbus.domain.defn.extension.StyleConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.StyleState;
import com.antheminc.oss.nimbus.domain.model.state.extension.conditionals.AssignThenToTargetCaseConditionalHandler;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Tony Lopez
 *
 */
public class StyleConditionalStateEventHandler extends AssignThenToTargetCaseConditionalHandler<StyleConditional> {

	public static final JustLogit LOG = new JustLogit();

	public StyleConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	protected void executeElse(Param<?> onChangeParam, Annotation configuredAnnotation, Set<Param<?>> targetParams) {
		for(Param<?> targetParam : targetParams) {
			targetParam.setStyle(null);
		}
	}

	@Override
	protected void whenConditionTrue(Object thenValue, Param<?> onChangeParam, Param<?> targetParam) {
		Style thenAnnotation = (Style) thenValue;
		StyleState styleState = new StyleState();
		styleState.setCssClass(thenAnnotation.cssClass());
		targetParam.setStyle(styleState);
	}
}
