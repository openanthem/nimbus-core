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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.TriConsumer;

import com.antheminc.oss.nimbus.AnnotationUtil;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.extension.AbstractConditionalStateEventHandler;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * <p>A conditional state event handler support class that performs subclass
 * logic depending on the result of the conditional evaluation.
 * 
 * <p>The execution offered by extending this class similar to a simple if/else
 * block in the Java programming language. Subclasses are able to define the
 * logic that should be executed when the condition evaluates to {@code true}
 * and similarly when the condition evaluates to {@code false}.
 * 
 * @author Tony Lopez
 * @since 1.3
 *
 */
public abstract class IfElseConditionalStateEventHandler<A extends Annotation>
		extends AbstractConditionalStateEventHandler<A> {

	public static final String ATTR_TARGET_PATH = "targetPath";
	public static final String ATTR_WHEN = "when";

	public IfElseConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>Evaluate the {@code when} expression given in the the
	 * {@code configuredAnnotation} using the {@contextParam} as the context, or
	 * the relative param this expression will be executed from.
	 * @param expression the expression to evaluate
	 * @param contextParam the relative param to execute the expression from
	 * @return the result of the expression evaluation
	 */
	protected boolean evaluateWhen(Annotation configuredAnnotation, Param<?> contextParam) {
		return evaluate(getAttributeWhen(configuredAnnotation), contextParam);
	}

	/**
	 * <p>Retrieve the {@code targetPath} attribute from the provided
	 * {@code configuredAnnotation}.<p>Uses annotation reflection support to
	 * retrieve the {@code targetPath} attribute. If the attribute is not
	 * present, an exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @return the attribute value
	 */
	protected String[] getAttributeTargetPath(Annotation configuredAnnotation) {
		return AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_TARGET_PATH, String[].class);
	}

	/**
	 * <p>Retrieve the {@code when} attribute from the provided
	 * {@code configuredAnnotation}.<p>Uses annotation reflection support to
	 * retrieve the {@code when} attribute. If the attribute is not present, an
	 * exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @return the attribute value
	 */
	protected String getAttributeWhen(Annotation configuredAnnotation) {
		return AnnotationUtil.safelyRetrieveAnnotationAttribute(configuredAnnotation, ATTR_WHEN, String.class);
	}

	/**
	 * <p>Retrieve the params relative to {@code contextParam} from the provided
	 * {@code targetPath} attribute of {@code configuredAnnotation}.<p>Uses
	 * annotation reflection support to retrieve the {@code targetPath}
	 * attribute. If the attribute is not present, an exception will be thrown.
	 * @param configuredAnnotation the conditional annotation
	 * @param contextParam the relative param to retrieve target params from
	 * @return the relative target params
	 */
	protected Set<Param<?>> getTargetPathParams(Annotation configuredAnnotation, Param<?> contextParam) {
		return Stream.of(getAttributeTargetPath(configuredAnnotation))
				.map(targetPath -> retrieveParamByPath(contextParam, targetPath)).collect(Collectors.toSet());
	}

	/**
	 * <p>Execute the conditional logic provided in {@code configuredAnnotation}
	 * and based on it's result, execute subclass logic for each target param
	 * also provided in {@code configuredAnnotation}. <p>This method is
	 * effectively the same as an if/else block, where the logic to execute when
	 * a condition is true is delegated to subclasses.
	 * @param onChangeParam the source parameter (annotated field)
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 * @return the result of the conditional evaluation
	 */
	@SuppressWarnings("unchecked")
	protected boolean handleConditionalExecution(Param<?> onChangeParam, Annotation configuredAnnotation,
			Set<Param<?>> targetParams) {
		boolean result = evaluateWhen(configuredAnnotation, onChangeParam);
		TriConsumer<A, Param<?>, Param<?>> consumer = this::whenFalse;
		if (result) {
			consumer = this::whenTrue;
		}

		for (Param<?> targetParam : targetParams) {
			consumer.accept((A) configuredAnnotation, onChangeParam, targetParam);
		}

		return result;
	}

	@Override
	protected void handleInternal(Param<?> onChangeParam, A configuredAnnotation) {
		handleConditionalExecution(onChangeParam, configuredAnnotation,
				this.getTargetPathParams(configuredAnnotation, onChangeParam));
	}

	/**
	 * <p>Execute the logic that should occur when this conditional state event
	 * handler evaluates to {@code false}.
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the param to execute the logic on
	 */
	protected abstract void whenFalse(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam);

	/**
	 * <p>Execute the logic that should occur when this conditional state event
	 * handler evaluates to {@code true}.
	 * @param configuredAnnotation the conditional annotation decorating
	 *            {@code onChangeParam}
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the param to execute the logic on
	 */
	protected abstract void whenTrue(A configuredAnnotation, Param<?> onChangeParam, Param<?> targetParam);
}
