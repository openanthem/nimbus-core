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

import com.antheminc.oss.nimbus.AnnotationUtil;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p>This class can be used for conditional event handlers that need the
 * following support:
 * 
 * <ol> <li>Evaluate a SpEL expression and when {@code true}, execute some
 * logic.</li> <li>When no conditional logic is applied, execute some default
 * logic.</li> </ol> conditional behavior
 * 
 * @author Tony Lopez
 * @since 1.1
 *
 * @param <A> the annotation to provide support for
 */
public abstract class EvalExprWithCrudDefaults<A extends Annotation> extends EvalExprWithCrudActions<A> {

	private static final String ATTR_TARGET_PATH = "targetPath";
	private static final String ATTR_CONDITION = "condition";
	private static final String ATTR_WHEN = "when";
	private static final String ATTR_THEN = "then";
	private static final String ATTR_EXCLUSIVE = "exclusive";

	public EvalExprWithCrudDefaults(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>Execute the conditional logic for this handler by invoking
	 * {@link #executeOnWhenConditionTrue(Object, Param, Param)} whenever a
	 * <i>condition's</i> (defined within {@code configuredAnnotation})
	 * {@code when} condition is {@code true}. <p>This method expects the
	 * following conditions be satisfied: <ul>
	 * <li>{@code configuredAnnotation.condition()} contains an attribute
	 * entitled {@code exclusive}.</li> <li>{@code configuredAnnotation}
	 * contains an attribute entitled {@code condition}.</li>
	 * <li>{@code configuredAnnotation.condition()} contains an attribute
	 * entitled {@code when}.</li> <li>{@code configuredAnnotation.condition()}
	 * contains an attribute entitled {@code then}.</li> </ul> <p>If
	 * {@code configuredAnnotation.exclusive()} is {@code true}, then only the
	 * first {@code true} <i>condition</i> will be executed. If {@code false},
	 * then all <i>conditions</i> will be executed.
	 * @param configuredAnnotation the annotation decorating
	 *            {@code onChangeParam}
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 * @return {@code true} when conditional logic was executed successfully,
	 *         {@code false} otherwise. If {@code false} is returned, the
	 *         defined default logic may be executed. See
	 *         {@link #executeDefault()} for more details.
	 */
	protected boolean executeConditional(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam) {
		boolean executed = false;

		Object[] oConditions = AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_CONDITION,
				Object[].class);
		boolean isExclusive = AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_EXCLUSIVE,
				Boolean.class);
		
		// iterate through the conditions
		for (final Object oCondition : oConditions) {

			Annotation condition = (Annotation) oCondition;
			String conditionWhen = AnnotationUtil.safelyRetrieveAnnotationAttribute(condition, ATTR_WHEN, String.class);
			// if the condition is evaluated to true
			if (this.evalWhen(onChangeParam, conditionWhen)) {

				// fire the true condition logic -- deferred to subclass
				// implementation
				Object conditionThen = AnnotationUtil.safelyRetrieveAnnotationAttribute(condition, ATTR_THEN,
						Object.class);
				executeOnWhenConditionTrue(conditionThen, onChangeParam, targetParam);

				// if the configured annotation should only execute one true
				// condition,
				// execute the first and exit
				if (isExclusive) {
					return true;
				}

				// note that a successful execution has occurred and
				// continue
				executed = true;
			}
		}

		return executed;
	}

	/**
	 * <p>Execute the default logic for this handler. <p>Logic defined within
	 * overridden methods will execute once if and only if the result of
	 * {@link #executeConditional(Annotation, Param, Param)} is {@code true}.
	 * Subclasses should take care to ensure the correct value is returned from
	 * this method to ensure the desired behavior is achieved.
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 */
	protected abstract void executeDefault(Param<?> onChangeParam, Param<?> targetParam);

	/**
	 * <p>Execute the primary conditional logic for this handler. <p>This method
	 * will be invoked whenever the configured annotation for
	 * {@code onChangeParam} has a condition containing a SpEL expression
	 * evaulating to {@code true}.
	 * @param payload the {@code then} value from the corresponding {@code true}
	 *            annotation condition SpEL expression that has been executed
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 */
	protected abstract void executeOnWhenConditionTrue(Object payload, Param<?> onChangeParam, Param<?> targetParam);

	/**
	 * <p>Execute either the conditional logic or default logic based on the
	 * configuration provided in {@code configuredAnnotation}. <p>This method
	 * seeks to retrieve the target parameter identified by
	 * {@code configuredAnnotation} and will attempt to apply logic in
	 * accordance to the rules of the subclass and configuration contained
	 * within {@code configuredAnnotation}.
	 * @param onChangeParam the source parameter (annotated field)
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 */
	@Override
	protected void handleInternal(Param<?> onChangeParam, A configuredAnnotation) {

		String targetPath = AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_TARGET_PATH,
				String.class);

		handleInternal(onChangeParam, targetPath, targetParam -> {

			final boolean shouldExecuteDefault = !this.executeConditional(configuredAnnotation, onChangeParam,
					targetParam);

			if (shouldExecuteDefault) {
				executeDefault(onChangeParam, targetParam);
			}
		});
	}
}
