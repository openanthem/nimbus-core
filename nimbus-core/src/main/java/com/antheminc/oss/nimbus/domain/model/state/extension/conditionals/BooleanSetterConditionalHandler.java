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
package com.antheminc.oss.nimbus.domain.model.state.extension.conditionals;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p>A very simple conditional state event handler support class that sets a
 * boolean value to the target {@link Param} object when the provided condition
 * evaluates to {@code true} and performs the inverse action when the condition
 * evaluates to {@code false}. Subclasses can control which property of the
 * {@link Param} object is set.
 * 
 * <p>The execution offered by extending this class similar to a simple if/else
 * block in the Java programming language.
 * 
 * @author Tony Lopez
 * @since 1.3
 *
 */
public abstract class BooleanSetterConditionalHandler<A extends Annotation>
		extends IfElseConditionalStateEventHandler<A> {

	public BooleanSetterConditionalHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>Get the setter method to invoke on {@code targetParam} when this
	 * conditional state event handler is {@code true} or {@code false}.
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to invoke the action on
	 * @return the setter method to invoke
	 */
	protected abstract Consumer<Boolean> setter(Param<?> targetParam);

	@Override
	protected void whenFalse(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam) {
		setter(targetParam).accept(false);
	}

	@Override
	protected void whenTrue(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam) {
		setter(targetParam).accept(true);
	}
}
