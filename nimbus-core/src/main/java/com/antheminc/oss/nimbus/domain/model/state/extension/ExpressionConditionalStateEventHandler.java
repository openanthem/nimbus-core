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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ExpressionConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
public class ExpressionConditionalStateEventHandler extends EvalExprWithCrudActions<ExpressionConditional> {

	public ExpressionConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ExpressionConditional configuredAnnotation) {
		boolean isExecuteThen = evalWhen(onChangeParam, configuredAnnotation.when());
		
		if(isExecuteThen) {
			String thenExpr = Optional.ofNullable(configuredAnnotation.then())
								.filter(StringUtils::isNotEmpty)
								.orElseThrow(()->new InvalidConfigException(configuredAnnotation+" must have valid expression to execute."));
			
			execute(onChangeParam, thenExpr);
			return;
		} 

//		// else expression
//		String elseExpr = StringUtils.trimToNull(configuredAnnotation.elseThen());
//		if(elseExpr == null) 
//			return;
//		
//		execute(onChangeParam, elseExpr);
	}
	
	private void execute(Param<?> onChangeParam, String expr) {
		getExpressionEvaluator().getValue(expr, new ParamStateHolder<>(onChangeParam));
	}
}
