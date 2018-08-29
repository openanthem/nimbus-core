/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.List;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
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
		List<LabelState> result = buildLabelConfigs(configuredAnnotation, param, param);
		if(result != null)
			param.setLabels(null);
		else
			logIt.warn(() -> "label configs lookup returned null for param "+param+" with config "+configuredAnnotation);		
	}

	@Override
	public List<LabelState> buildLabelConfigs(Label label, Param<?> srcParam, Param<?> targetParam) {
		return null;//srcParam.getConfig().getLabels();
	}

}
