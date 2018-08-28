/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.LabelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;

/**
 * @author Swetha Vemuri
 *
 */
@Getter
public class DefaultLabelConfigsHandler implements LabelConfigsOnLoadHandler {
	
	private final JustLogit logIt = new JustLogit(DefaultLabelConfigsHandler.class);

	protected final CommandPathVariableResolver pathVariableResolver;
	protected final CommandExecutorGateway gateway;
	
	public DefaultLabelConfigsHandler(BeanResolverStrategy beanResolver) {
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
	}
	
	@Override
	public void handle(Label configuredAnnotation, Param<?> param) {
		List<LabelConfig> result = buildLabelConfigs(configuredAnnotation, param, param);
		if(result != null)
			param.setLabels(result);
		else
			logIt.warn(() -> "label configs lookup returned null for param "+param+" with config "+configuredAnnotation);		
	}

	@Override
	public List<LabelConfig> buildLabelConfigs(Label label, Param<?> srcParam, Param<?> targetParam) {
		List<LabelConfig> labelConfigs = new ArrayList<>();
		if (null == srcParam || null == srcParam.getConfig() || null == srcParam.getConfig().getLabelConfigs()) {
			return labelConfigs;
		}
		
		for (LabelConfig labelConfig : srcParam.getConfig().getLabelConfigs()) {
			LabelConfig toAdd = new LabelConfig();
			BeanUtils.copyProperties(labelConfig, toAdd);
			labelConfigs.add(toAdd);
		}
		return labelConfigs;
	}

}
