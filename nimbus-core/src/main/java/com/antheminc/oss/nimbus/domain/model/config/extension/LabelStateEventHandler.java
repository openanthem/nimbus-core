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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.extension.Content;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */

public class LabelStateEventHandler extends AbstractConfigEventHandler<Label> implements OnStateLoadHandler<Label> {

	@Autowired 
	CommandPathVariableResolver cmdPathResolver;
	
	@Override
	public void handle(Label configuredAnnotation, Param<?> param) {
		if(configuredAnnotation==null)
			return;
		LabelState labelState = convert(configuredAnnotation, param);
		validateAndAdd(param, labelState);
	}
	
	protected void validateAndAdd(Param<?> param, LabelState labelState) {

		// duplicate check the previous label assigned. 
		if(CollectionUtils.isNotEmpty(param.getLabels())) {
			if (param.getLabels().stream().anyMatch(l -> l.getLocale().equals(labelState.getLocale()))) { 
				return; 
			}
		}
		
		// at-least one of Label text or helpText must be present
		if(StringUtils.isEmpty(labelState.getText()) && StringUtils.isEmpty(labelState.getHelpText()))
			throw new InvalidConfigException("Label must have non empty values for at least label text value or help text,"
					+ " found none for \"" + param.getConfig().getCode() + "\" in Param: " + param
					+ " with LabelState: " + labelState);
		
		Set<LabelState> labels = new HashSet<>();
		if(!CollectionUtils.isEmpty(param.getLabels())) 
			labels.addAll(param.getLabels());
		
		if(param.isCollection()) {		
			Map<String, Set<LabelState>> elemLabels = new HashMap<>(); 
			ParamConfig<?> p = param.getConfig().getType().findIfCollection().getElementConfig(); 
			
			p.getType().findIfNested().getModelConfig().getParamConfigs().forEach(ec -> {		
				
				if(CollectionUtils.isNotEmpty(ec.getLabels())) {					
					Set<LabelState> listParamLabels = new HashSet<>();					
					ec.getLabels().forEach((label) -> {
						listParamLabels.add(convert(label, param));
					});				
					elemLabels.put(ec.getId(),listParamLabels);
				}				
			});
			
			param.findIfCollection().setElemLabels(elemLabels);
		}
		
		labels.add(labelState);
		param.setLabels(labels);
	}
	
	protected LabelState convert(Label label, Param<?> param) {
		LabelState labelState = new LabelState();
		
		Locale locale = Content.getLocale(label);
		
		labelState.setLocale(StringUtils.trimToNull(locale.toLanguageTag()));
		labelState.setText(resolvePath(StringUtils.isWhitespace(label.value()) ? label.value() : StringUtils.trimToNull(label.value()), param));	
		labelState.setHelpText(resolvePath(StringUtils.trimToNull(label.helpText()),param));
		labelState.setCssClass(StringUtils.trimToNull(label.style().cssClass()));
		
		return labelState;
	}
	
	protected String resolvePath(String text, Param<?> param) {
		String resolvedPath = this.cmdPathResolver.resolve(param, text);
		return resolvedPath;
	}
}
