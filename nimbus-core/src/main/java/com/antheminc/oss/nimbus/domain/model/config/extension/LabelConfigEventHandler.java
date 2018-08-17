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

import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.extension.Content;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.LabelConfig;
import com.antheminc.oss.nimbus.domain.model.config.event.ConfigEventHandlers.OnParamCreateHandler;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultParamConfig;

/**
 * @author Soham Chakravarti
 *
 */

public class LabelConfigEventHandler extends AbstractConfigEventHandler<Label> implements OnParamCreateHandler<Label> {

	@Override
	public void handle(Label configuredAnnotation, ParamConfig<?> param) {
		if(configuredAnnotation==null)
			return;
		
		DefaultParamConfig<?> paramConfig = castOrEx(DefaultParamConfig.class, param);
		
		if (null == paramConfig) {
			throw new FrameworkRuntimeException("Retrieved paramConfig for " + param + " was null.");
		}
		
		if (null == paramConfig.getLabelConfigs()) {
			paramConfig.setLabelConfigs(new ArrayList<>());
		}
		
		validateAndAdd(paramConfig, convert(configuredAnnotation));
	}
	
	protected void validateAndAdd(ParamConfig<?> paramConfig, LabelConfig toAdd) {
		// duplicate check
		paramConfig.getLabelConfigs().stream()
			.filter(lc->lc.getLocale().equals(toAdd.getLocale()))
			.forEach(lc->{
				throw new InvalidConfigException("Label must have unique entries by locale,"
						+ " found multiple entries in ParamConfig: "+paramConfig
						+ " with repeating locale for LabelConfig: "+ toAdd);	
			});
		
		// at-least one of Label text or helpText must be present
		if(StringUtils.isEmpty(toAdd.getText()) && StringUtils.isEmpty(toAdd.getHelpText()))
			throw new InvalidConfigException("Label must have non empty values for at least label text value or help text,"
					+ " found none for \"" + paramConfig.getCode() + "\" in ParamConfig: " + paramConfig
					+ " with LabelConfig: " + toAdd);
		
		paramConfig.getLabelConfigs().add(toAdd);
	}
	
	protected LabelConfig convert(Label label) {
		LabelConfig config = new LabelConfig();
		
		Locale locale = Content.getLocale(label);
		
		config.setLocale(StringUtils.trimToNull(locale.toLanguageTag()));
		config.setText(StringUtils.isWhitespace(label.value()) ? label.value() : StringUtils.trimToNull(label.value()));	
		config.setHelpText(StringUtils.trimToNull(label.helpText()));
		
		return config;
	}
}
