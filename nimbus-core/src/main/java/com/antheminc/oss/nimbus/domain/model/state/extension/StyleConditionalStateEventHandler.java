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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.Style;
import com.antheminc.oss.nimbus.domain.defn.extension.StyleConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.StyleState;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Tony Lopez
 *
 */
public class StyleConditionalStateEventHandler extends EvalExprWithCrudDefaults<StyleConditional> {

	public static final JustLogit LOG = new JustLogit();

	public StyleConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	protected void applyStyleToState(Param<?> onChangeParam, Param<?> targetParam,
			Style style) {
		StyleState styleState = new StyleState();
		styleState.setCssClass(style.cssClass());
		targetParam.setStyle(styleState);
	}

	@Override
	protected void executeDefault(Param<?> onChangeParam, Param<?> targetParam) {
		targetParam.setStyle(null);
	}

	@Override
	protected void executeOnWhenConditionTrue(Object payload, Param<?> onChangeParam, Param<?> targetParam) {
		// Convert the payload into the expected parameter type.
		Style style = (Style) payload;
		this.applyStyleToState(onChangeParam, targetParam, style);
	}
}
