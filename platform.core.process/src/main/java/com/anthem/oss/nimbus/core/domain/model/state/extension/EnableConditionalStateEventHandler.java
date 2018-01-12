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
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.extension.EnableConditional;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class EnableConditionalStateEventHandler extends AbstractConditionalStateEventHandler.EvalExprWithCrudActions<EnableConditional> {

	public EnableConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override	
	protected void handleInternal(Param<?> onChangeParam, EnableConditional configuredAnnotation) {
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		// validate target param to enable
		String[] targetPaths = configuredAnnotation.targetPath();

		Arrays.asList(targetPaths).stream()
			.forEach(targetPath -> handleInternal(onChangeParam, targetPath, targetParam->targetParam.setEnabled(isTrue)));
		
	}
}
