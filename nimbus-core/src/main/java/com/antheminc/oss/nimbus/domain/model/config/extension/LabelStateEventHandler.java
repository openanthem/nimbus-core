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
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.extension.Content;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class LabelStateEventHandler extends AbstractConfigEventHandler implements OnStateLoadHandler<Label> {

	@Autowired
	CommandPathVariableResolver cmdPathResolver;

	/**
	 * <p>Add the label from {@code configuredAnnotation} to the label state of
	 * {@code targetParam}.
	 * @param configuredAnnotation the label annotation to add
	 * @param targetParam the param to add the label to
	 */
	public void addLabelToState(Label configuredAnnotation, Param<?> targetParam) {
		addLabelToState(configuredAnnotation, targetParam, targetParam);
	}

	/**
	 * <p>Add the label from {@code configuredAnnotation} to the label state of
	 * {@code targetParam}, using {@code contextParam} as the "context" from
	 * which any param path will be retrieved.
	 * @param configuredAnnotation the label annotation to add
	 * @param targetParam the param to add the label to
	 * @param contextParam the "context" from which any param path information
	 *            should be retrieved
	 */
	public void addLabelToState(Label configuredAnnotation, Param<?> targetParam, Param<?> contextParam) {
		if (configuredAnnotation == null) {
			return;
		}

		LabelState labelState = convert(configuredAnnotation, contextParam);
		validateAndAdd(labelState, targetParam, contextParam);
	}

	/**
	 * <p>Convert the provided {@code label} into it's {@link LabelState}
	 * equivalent object. <p>Some properties within the resulting
	 * {@link LabelState} object will be resolved using the
	 * {@link CommandPathVariableResolver}. See the implementation for more
	 * details.
	 * @param label the {@link Label} object to convert
	 * @param param the {@link Param} from which to resolve pathing information
	 *            from
	 * @return the converted {@link LabelState} object.
	 */
	public LabelState convert(Label label, Param<?> param) {
		LabelState labelState = new LabelState();

		Locale locale = Content.getLocale(label);

		labelState.setLocale(StringUtils.trimToNull(locale.toLanguageTag()));
		labelState.setText(resolvePath(
				StringUtils.isWhitespace(label.value()) ? label.value() : StringUtils.trimToNull(label.value()),
				param));

		labelState.setHelpText(resolvePath(StringUtils.trimToNull(label.helpText()), param));
		labelState.setCssClass(StringUtils.trimToNull(label.style().cssClass()));

		return labelState;
	}

	@Override
	public void onStateLoad(Label configuredAnnotation, Param<?> param) {
		addLabelToState(configuredAnnotation, param);
	}

	protected String resolvePath(String text, Param<?> param) {
		String resolvedPath = this.cmdPathResolver.resolve(param, text);
		return resolvedPath;
	}

	/**
	 * <p>Add {@code labelState} to the label state of {@code targetParam}.
	 * <p>The argument {@code contextParam} is used to provide the "context"
	 * from which to retrieve pathing details when resolving any framework or
	 * placeholders within the previously provided {@link Label}. If this level
	 * of control is not needed, consider using
	 * {@link #validateAndAdd(LabelState, Param)}.
	 * @param labelState the label state to add
	 * @param contextParam the "context" from which any param path information
	 *            should be retrieved
	 * @param targetParam the param to add the label to
	 */
	protected void validateAndAdd(LabelState labelState, Param<?> targetParam, Param<?> contextParam) {

		// duplicate check the previous label assigned.
		if (CollectionUtils.isNotEmpty(targetParam.getLabels())) {
			if (targetParam.getLabels().stream().anyMatch(l -> l.getLocale().equals(labelState.getLocale()))) {
				return;
			}
		}

		// at-least one of Label text or helpText must be present
		if (StringUtils.isEmpty(labelState.getText()) && StringUtils.isEmpty(labelState.getHelpText()))
			throw new InvalidConfigException(
					"Label must have non empty values for at least label text value or help text,"
							+ " found none for \"" + targetParam.getConfig().getCode() + "\" in Param: " + targetParam
							+ " with LabelState: " + labelState);

		Set<LabelState> labels = new HashSet<>();
		if (!CollectionUtils.isEmpty(targetParam.getLabels()))
			labels.addAll(targetParam.getLabels());

		if (targetParam.isCollection()) {
			Map<String, Set<LabelState>> elemLabels = new HashMap<>();
			ParamConfig<?> p = targetParam.getConfig().getType().findIfCollection().getElementConfig();

			p.getType().findIfNested().getModelConfig().getParamConfigs().forEach(ec -> {

				if (CollectionUtils.isNotEmpty(ec.getLabels())) {
					Set<LabelState> listParamLabels = new HashSet<>();
					ec.getLabels().forEach((label) -> {
						listParamLabels.add(convert(label, contextParam));
					});
					elemLabels.put(ec.getId(), listParamLabels);
				}
			});

			targetParam.findIfCollection().setElemLabels(elemLabels);
		}

		labels.add(labelState);
		targetParam.setLabels(labels);
	}
}
