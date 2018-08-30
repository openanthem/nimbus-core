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
/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.extension.Content;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */

public class LabelStateEventHandler extends AbstractConfigEventHandler<Label> implements OnStateLoadHandler<Label> {

	@Override
	public void handle(Label configuredAnnotation, Param<?> param) {
		if(configuredAnnotation==null)
			return;
		
		validateAndAdd(param, convert(configuredAnnotation));
	}
	
	protected void validateAndAdd(Param<?> param, LabelState toAdd) {

		// duplicate check the previous label assigned. 
		if(CollectionUtils.isNotEmpty(param.getLabels())) {
			if (param.getLabels().stream().anyMatch(l -> l.getLocale().equals(toAdd.getLocale()))) { 
				return; 
			}
		}
		// TODO: Move this validation to @Label annotation preprocessor
		// at-least one of Label text or helpText must be present
		if(StringUtils.isEmpty(toAdd.getText()) && StringUtils.isEmpty(toAdd.getHelpText()))
			throw new InvalidConfigException("Label must have non empty values for at least label text value or help text,"
					+ " found none for \"" + param.getConfig().getCode() + "\" in Param: " + param
					+ " with LabelState: " + toAdd);
		
		Set<LabelState> labels = new HashSet<>();
		if(!CollectionUtils.isEmpty(param.getLabels())) 
			labels.addAll(param.getLabels());
		
		labels.add(toAdd);
		param.setLabels(labels);
	}
	
	protected LabelState convert(Label label) {
		LabelState config = new LabelState();
		
		Locale locale = Content.getLocale(label);
		
		config.setLocale(StringUtils.trimToNull(locale.toLanguageTag()));
		config.setText(StringUtils.isWhitespace(label.value()) ? label.value() : StringUtils.trimToNull(label.value()));	
		config.setHelpText(StringUtils.trimToNull(label.helpText()));
		config.setCssClass(StringUtils.trimToNull(label.style().cssClass()));
		
		return config;
	}
}
