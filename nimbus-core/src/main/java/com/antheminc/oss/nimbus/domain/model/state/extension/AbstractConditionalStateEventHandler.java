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
import java.util.EnumSet;
import java.util.Optional;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * <p>A conditional state event handler support class that aids in performing
 * subclass logic depending on the result of the conditional evaluation.
 * 
 * @author Soham Chakravarti
 * @author Tony Lopez
 * @since 1.0
 *
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractConditionalStateEventHandler<A extends Annotation>
		extends AbstractEventHandlerSupport<A> {

	private static final JustLogit LOG = new JustLogit(AbstractConditionalStateEventHandler.class);

	protected final BeanResolverStrategy beanResolver;
	protected final ExpressionEvaluator expressionEvaluator;

	public AbstractConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}

	@Override
	public void onStateChange(A configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		EnumSet<Action> validSet = EnumSet.of(Action._new, Action._update, Action._replace, Action._delete);

		if (!validSet.contains(event.getAction()))
			return;

		conditionalLogWrapper(configuredAnnotation, StateChangeEventType.ON_CHANGE,
				() -> handleInternal(event.getParam(), configuredAnnotation, StateChangeEventType.ON_CHANGE));
	}

	@Override
	public void onStateLoad(A configuredAnnotation, Param<?> param) {
		conditionalLogWrapper(configuredAnnotation, StateChangeEventType.ON_LOAD,
				() -> handleInternal(param, configuredAnnotation, StateChangeEventType.ON_LOAD));
	}

	@Override
	public void onStateLoadNew(A configuredAnnotation, Param<?> param) {
		conditionalLogWrapper(configuredAnnotation, StateChangeEventType.ON_LOAD_NEW,
				() -> handleInternal(param, configuredAnnotation, StateChangeEventType.ON_LOAD_NEW));
	}

	private void conditionalLogWrapper(A configuredAnnotation, StateChangeEventType eventType, Runnable r) {
		LOG.trace(
				() -> "=== START conditional " + eventType.name() + " handler execution for: " + configuredAnnotation);
		r.run();
		LOG.trace(() -> "=== END conditional " + eventType.name() + " handler execution for: " + configuredAnnotation);
	}

	/**
	 * <p>Evaluate the given expression using the {@contextParam} as the
	 * context, or the relative param this expression will be executed from.
	 * @param expression the expression to evaluate
	 * @param contextParam the relative param to execute the expression from
	 * @return the result of the expression evaluation
	 */
	protected boolean evaluate(String expression, Param<?> contextParam) {
		try {
			boolean result = getExpressionEvaluator().getValue(expression, new ParamStateHolder<>(contextParam),
					Boolean.class);
			LOG.trace(() -> "\"" + expression + "\" evaluated to " + String.valueOf(result).toUpperCase()
					+ ". Context param: " + contextParam);
			return result;
		} catch (Exception e) {
			throw new FrameworkRuntimeException("Encountered an exception evaluating the expression \"" + expression
					+ "\" + for param: " + contextParam, e);
		}
	}

	/**
	 * <p>Execute the logic for this state event handler.
	 * @param onChangeParam the param that for which te state event handler has
	 *            been triggered
	 * @param configuredAnnotation the state change handler annotation
	 */
	protected abstract void handleInternal(Param<?> onChangeParam, A configuredAnnotation);

	protected void handleInternal(Param<?> onChangeParam, A configuredAnnotation,
			StateChangeEventType stateChangeEventType) {
		setStateChangeEventType(stateChangeEventType);
		handleInternal(onChangeParam, configuredAnnotation);
	}

	/**
	 * <p>Find the param from the given {@code targetPath} relative to the
	 * {@code contextParam}.
	 * @param contextParam the relative param to retrieve the target param from
	 * @param targetPath the path to the param
	 * @return the param
	 */
	protected Param<?> retrieveParamByPath(Param<?> contextParam, String targetPath) {
		return Optional.ofNullable(contextParam.findParamByPath(targetPath))
				.orElseThrow(() -> new InvalidConfigException("Target param lookup returned null for targetPath: "
						+ targetPath + " on param: " + contextParam));
	}
}
