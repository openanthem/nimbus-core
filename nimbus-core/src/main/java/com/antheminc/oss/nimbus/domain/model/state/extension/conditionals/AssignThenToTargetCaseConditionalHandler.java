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
import java.util.Set;

import com.antheminc.oss.nimbus.AnnotationUtil;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * <p>A conditional state event handler support class that executes one or more
 * conditional statements and depending on the outcome of the executed
 * conditional statement(s), performs some action with a configured value
 * (typically into a {@link Param} object).
 * 
 * <p>This conditional state event handler is similar to an if/else if/else or
 * case block in the Java programming language. The provided conditional
 * annotations will be evaluated and when one is {@code true}, support is
 * included to set the value provided for {@code then} into the target
 * {@link Param}.
 * 
 * <p>"Case" like behavior can be achieved by setting the {@code exclusive} to
 * {@code false}, which allows for attribute on the configured annotation. By
 * default this value is {@code true}, so providing multiple conditions will
 * result in logic being executed in "else if" fashion.
 * 
 * @author Tony Lopez
 * @since 1.3
 *
 * @param <A> the annotation to provide support for
 */
public abstract class AssignThenToTargetCaseConditionalHandler<A extends Annotation>
		extends IfElseConditionalStateEventHandler<A> {

	private static final JustLogit LOG = new JustLogit(AssignThenToTargetCaseConditionalHandler.class);
	public static final String ATTR_CONDITION = "condition";
	public static final String ATTR_THEN = "then";
	public static final String ATTR_EXCLUSIVE = "exclusive";

	public AssignThenToTargetCaseConditionalHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>Execute the default logic for this handler. <p>Logic defined within
	 * overridden methods will execute once if and only any of the provided
	 * conditions do not execute.
	 * @param onChangeParam the source parameter (annotated field)
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 * @param targetParams the target parameter(s) to execute against
	 */
	protected void executeElse(Param<?> onChangeParam, Annotation configuredAnnotation, Set<Param<?>> targetParams) {
		// Do nothing by default
	}

	/**
	 * <p>Retrieve the {@code condition} attribute from the provided
	 * {@code configuredAnnotation}.<p>Uses annotation reflection support to
	 * retrieve the {@code condition} attribute. If the attribute is not
	 * present, an exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @return the attribute value
	 */
	protected Annotation[] getAttributeCondition(Annotation configuredAnnotation) {
		return AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_CONDITION,
				Annotation[].class);
	}
	
	/**
	 * <p>Retrieve the {@code exclusive} attribute from the provided
	 * {@code configuredAnnotation}.<p>Uses annotation reflection support to
	 * retrieve the {@code exclusive} attribute. If the attribute is not
	 * present, an exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @return the attribute value
	 */
	protected boolean getAttributeExclusive(Annotation configuredAnnotation) {
		return AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_EXCLUSIVE, Boolean.class);
	}

	/**
	 * <p>Retrieve the {@code then} attribute from the provided
	 * {@code configuredAnnotation}.<p>Uses annotation reflection support to
	 * retrieve the {@code then} attribute. If the attribute is not present, an
	 * exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @return the attribute value
	 */
	protected Object getAttributeThen(Annotation configuredAnnotation) {
		return AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_THEN, Object.class);
	}

	@Override
	protected void handleInternal(Param<?> onChangeParam, A configuredAnnotation) {
		boolean isMutuallyExclusive = getAttributeExclusive(configuredAnnotation);
		Annotation[] conditions = getAttributeCondition(configuredAnnotation);
		Set<Param<?>> targetParams = getTargetPathParams(configuredAnnotation, onChangeParam);

		boolean executed = false;
		for (final Annotation condition : conditions) {
			boolean result = handleConditionalExecution(onChangeParam, condition, targetParams);
			if (result) {
				if (isMutuallyExclusive) {
					return;
				}
				executed = true;
			}
		}

		if (!executed) {
			LOG.trace(() -> "Executing default expression for " + configuredAnnotation);
			executeElse(onChangeParam, configuredAnnotation, targetParams);
		}
	}

	/**
	 * <p>Execute the logic that should occur when this a conditional execution
	 * evaluates to {@code true}.
	 * @param thenValue the attribute value for {@code then} from the configured
	 *            conditional annotation
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the param to execute the logic on
	 */
	protected abstract void whenConditionTrue(Object thenValue, Param<?> onChangeParam, Param<?> targetParam);

	@Override
	protected void whenFalse(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam) {
		// do nothing, delegates execution to whether or not
		// configuredAnnotation is mutually exclusive, and if so executes {@link #executeElse}
	}

	@Override
	protected void whenTrue(A conditionalAnnotation, Param<?> onChangeParam, Param<?> targetParam) {
		whenConditionTrue(getAttributeThen(conditionalAnnotation), onChangeParam, targetParam);
	}
}
