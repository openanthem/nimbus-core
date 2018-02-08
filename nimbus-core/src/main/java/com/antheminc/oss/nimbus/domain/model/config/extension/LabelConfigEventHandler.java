/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.InvalidConfigException;
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
		
		Optional.ofNullable(paramConfig.getLabelConfigs()).orElseGet(()->{
			paramConfig.setLabelConfigs(new ArrayList<>());
			return paramConfig.getLabelConfigs();
		});
		
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
