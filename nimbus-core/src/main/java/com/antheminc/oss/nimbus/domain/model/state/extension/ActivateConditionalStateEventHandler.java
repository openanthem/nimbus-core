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

import java.util.Arrays;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
public class ActivateConditionalStateEventHandler extends EvalExprWithCrudActions<ActivateConditional> {

	public ActivateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ActivateConditional configuredAnnotation) {
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		// validate target param to activate
		String[] targetPaths = configuredAnnotation.targetPath();

		Arrays.asList(targetPaths).stream()
			.forEach(targetPath -> handleInternal(onChangeParam, targetPath, (targetParam->{
				if(isTrue)
					targetParam.activate();
				else
					targetParam.deactivate();
			})));
		
	}
	
}
