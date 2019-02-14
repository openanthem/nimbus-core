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

import java.lang.annotation.Annotation;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractConditionalStateEventHandler<A extends Annotation> extends AbstractEventHandlerSupport<A> {

	protected BeanResolverStrategy beanResolver;
	protected ExpressionEvaluator expressionEvaluator;
	@Setter(AccessLevel.PROTECTED)
	protected StateEventType stateEventType;
	
	public static enum StateEventType {
		ON_LOAD,
		ON_CHANGE;
	}
	
	public AbstractConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}
	
	protected boolean evalWhen(Param<?> onChangeParam, String whenExpr) {
		return getExpressionEvaluator().getValue(whenExpr, new ParamStateHolder<>(onChangeParam), Boolean.class);
	}
	
	protected Param<?> retrieveParamByPath(Param<?> baseParam, String targetPath) {
		return Optional.ofNullable(baseParam.findParamByPath(targetPath))
				.orElseThrow(() -> new InvalidConfigException("Target param lookup returned null for targetPath: " + targetPath + " on param: " + baseParam));
	}
}
