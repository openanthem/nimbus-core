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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>ValuesConditional State Event handler implementation for updating
 * {@link Values} annotated fields based on conditional logic defined via
 * configuration during the {@link OnStateChange} event.</p>
 * 
 * <p>Handles the scenario for when {@link ValuesConditional} provides the
 * {@code resetOnChange} flag. When true, the handler will always reset the
 * state of the {@code targetParam} after the execution step when new
 * {@code values} are updated. If false, the handler will reset the state of the
 * {@code targetParam} only when the existing state does not exist within the
 * new updated set of {@link Values}.</p>
 * 
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class ValuesConditionalStateEventHandler extends EvalExprWithCrudDefaults<ValuesConditional> {

	protected static final JustLogit LOG = new JustLogit(ValuesConditionalStateEventHandler.class);
	private final ParamValuesOnLoadHandler paramValuesHandler;

	private boolean resetOnChange;

	public ValuesConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		paramValuesHandler = beanResolver.get(ParamValuesOnLoadHandler.class, "paramValuesHandler");
	}

	@Override
	public void onStateChange(ValuesConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		setResetOnChange(configuredAnnotation.resetOnChange());
		super.onStateChange(configuredAnnotation, txnCtx, event);
	}

	/**
	 * <p>Apply all logic that should be fired after
	 * {@link #applyValuesToState(Values, Param, Param)} is executed. <p>If
	 * {@link #resetOnChange} is {@code true}, then the state of
	 * {@code targetParam} will be set to {@code null}. <p>If the existing state
	 * of {@code targetParam} is not matching a value in the new set of
	 * {@link Values} in {@code targetParam.getValues()}, the state of
	 * {@code targetParam will be set to {@code null}. <p>This method is
	 * intended to be overridden by subclasses to extend additional
	 * functionality when needed.
	 * @param targetParam the entity to set the values within
	 */
	protected void afterExecute(Param<?> targetParam) {

		// If the target param is not enabled, skip the state processing. In
		// other words, we don't want ValuesConditional to affect the state of
		// the targetParam when it is not in an "enabled" state.
		if (!targetParam.isEnabled()) {
			return;
		}

		if (isResetOnChange()) {
			targetParam.setState(null);
		} else {

			// if there are no values set (default config values) OR
			// if previously selected targetParam state is not in the list of
			// new values. then reset to null.
			Object state = targetParam.getState() != null ? targetParam.getState() : null;
			if (state instanceof String && (null == targetParam.getValues() || (null != targetParam.getState() && !targetParam.getValues().stream()
					.map(ParamValue::getCode).collect(Collectors.toList()).contains(state.toString())))) {

				targetParam.setState(null);
			}
			// otherwise the state persists.
		}
	}

	/**
	 * <p>Apply the {@code values} field for {@code targetParam} by extracting
	 * the underlying collection of {@code ParamValue} defined within
	 * {@code values}.
	 * @param values the values metadata to set
	 * @param onChangeParam the param represented by the field decorated with
	 *            {@link ValuesConditional}
	 * @param targetParam the entity to set the values within
	 */
	protected void applyValuesToState(Values values, Param<?> onChangeParam, Param<?> targetParam) {
		final List<ParamValue> oldValues = targetParam.getValues();
		final List<ParamValue> newValues = getParamValuesHandler().buildParamValues(values, onChangeParam, targetParam);
		targetParam.setValues(newValues);
		LOG.trace(() -> "Updated values for param '" + targetParam + "' from '" + oldValues + "' to '" + newValues
				+ "'.");

		// perform any after-execution logic needed
		if (StateEventType.ON_CHANGE.equals(getStateEventType())) {
			this.afterExecute(targetParam);
		}
	}

	/**
	 * <p>Reset the values for {@code targetParam} using the {@link Values} that
	 * decorates the field representing it. <p>If the field represented by
	 * {@code targetParam} does not have a {@link Values} decorator, the label
	 * config for that param will be set to an empty {@link ArrayList}.
	 * @param onChangeParam the param represented by the field decorated with
	 *            {@link ValuesConditional}
	 * @param targetParam the target parameter to execute against
	 */
	@Override
	protected void executeDefault(Param<?> onChangeParam, Param<?> targetParam) {
		applyValuesToState(targetParam.getConfig().getValues(), onChangeParam, targetParam);
	}

	/**
	 * <p>Apply all {@link Values} within the configured annotation of
	 * {@link ValuesConditional} to {@code targetParam}.
	 * @param payload the {@link ValuesConditional.Condition#then()} value from
	 *            the corresponding {@code true} condition that has been
	 *            executed
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 */
	@Override
	protected void executeOnWhenConditionTrue(Object payload, Param<?> onChangeParam, Param<?> targetParam) {
		applyValuesToState((Values) payload, onChangeParam, targetParam);
	}
}
