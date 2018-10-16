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
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.LabelConditional;
import com.antheminc.oss.nimbus.domain.model.config.extension.LabelStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * <p>Label Conditional State Event handler for conditionally assigning labels
 * based on conditional logic defined via &#64;{@code LabelConditional}
 * configuration.
 * 
 * @see com.antheminc.oss.nimbus.domain.defn.extension.LabelConditional
 * @see com.antheminc.oss.nimbus.domain.defn.extension.LabelConditionals
 * @author Tony Lopez
 * @since 1.1
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
public class LabelConditionalStateEventHandler extends EvalExprWithCrudDefaults<LabelConditional> {

	public static final JustLogit LOG = new JustLogit(LabelConditionalStateEventHandler.class);

	private final LabelStateEventHandler labelHandler;

	public LabelConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		labelHandler = beanResolver.find(LabelStateEventHandler.class);
	}

	/**
	 * <p>Set the label config state of {@code targetParam} to the label config
	 * supplied by {@code supplier}. <p>The {@code supplier} is the primary
	 * provider of the label config data that will be set. If the need is
	 * straightforward, use {@code supplier} to simply return the desired
	 * result. Otherwise, the {@code targetParam} parameter can be used to
	 * retrieve any additional information about {@code targetParam} that should
	 * be used in determining what to return as the final collection of
	 * {@link LabelConfig} elements to set.
	 * @param onChangeParam the param represented by the field decorated with
	 *            {@link LabelConditional}
	 * @param targetParam the target parameter to execute against
	 * @param supplier the {@link Function} to execute and return the collection
	 *            of {@link LabelConfig} elements to set
	 */
	protected void applyLabelToState(Param<?> onChangeParam, Param<?> targetParam,
			Function<Param<?>, Set<Label>> supplier) {

		// Retrieve the labels to be added from the provided
		// supplier.
		Set<Label> labelsToAdd = supplier.apply(targetParam);

		// If there are labels to add, add them.
		if (null != labelsToAdd && !labelsToAdd.isEmpty()) {

			// Ensure we are working with an empty collection.
			targetParam.setLabels(new HashSet<>());

			// Add the new label objects.
			for (Label label : labelsToAdd) {
				getLabelHandler().addLabelToState(label, targetParam, onChangeParam);
			}

		} else {
			// If there are no labels to add, reset the state to null.
			targetParam.setLabels(null);
		}

		LOG.debug(() -> "Replaced label configs for " + targetParam + " with " + labelsToAdd);
	}

	/**
	 * <p>Reset the label configs for {@code targetParam} using the
	 * {@link Label} that decorates the field representing it. <p>If the field
	 * represented by {@code targetParam} does not have a {@link Label}
	 * decorator, the label config for that param will be set to an empty
	 * {@link ArrayList}.
	 * @param onChangeParam the param represented by the field decorated with
	 *            {@link LabelConditional}
	 * @param targetParam the target parameter to execute against
	 */
	@Override
	protected void executeDefault(Param<?> onChangeParam, Param<?> targetParam) {
		this.applyLabelToState(onChangeParam, targetParam, p -> p.getConfig().getLabels());
	}

	/**
	 * <p>Apply all {@code labels} within the configured annotation of
	 * {@link LabelConditional} to {@code targetParam}.
	 * @param payload the {@link LabelConditional.Condition#then()} value from
	 *            the corresponding {@code true} condition that has been
	 *            executed
	 * @param onChangeParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 */
	@Override
	protected void executeOnWhenConditionTrue(Object payload, Param<?> onChangeParam, Param<?> targetParam) {
		// Convert the payload into the expected parameter type.
		Label[] labelsArr = (Label[]) payload;

		// Add the conditional label configs from the LabelConditional
		// annotation.
		Set<Label> labels = new HashSet<>();
		for (Label label : labelsArr) {
			labels.add(label);
		}

		this.applyLabelToState(onChangeParam, targetParam, p -> labels);
	}
}
